package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.model.Attendance;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.util.ServiceUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.conexasaude.challenge.util.ServiceUtils.removeCpfMask;
import static br.com.conexasaude.challenge.util.ServiceUtils.removePhoneMask;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private PatientService patientService;


    private String getDoctorEmailFromAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private void mapEntityFromDTO(Doctor doctor, DoctorDTO doctorDTO) {
        doctorDTO.setCpf(removeCpfMask(doctorDTO.getCpf()));
        doctorDTO.setPhoneNumber(removePhoneMask(doctorDTO.getPhoneNumber()));

        ModelMapper modelMapper = new ModelMapper();
        TypeMap<DoctorDTO, Doctor> propertyMapper = modelMapper.createTypeMap(DoctorDTO.class, Doctor.class);
        propertyMapper.addMappings(mapper -> mapper.skip(Doctor::setPwd));
        modelMapper.map(doctorDTO, doctor);

        doctor.setPwd(passwordEncoder.encode(doctorDTO.getPwd()));
    }

    @Override
    public DoctorDTO register(DoctorDTO doctorDTO) {
        String cpf = ServiceUtils.removeCpfMask(doctorDTO.getCpf());
        String email = doctorDTO.getEmail();

        if (doctorRepository.existsByCpf(cpf) && doctorRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException(ApiMessages.EMAIL_AND_CPF_UNIQUE_CONSTRAINT_VIOLATION);

        } else if (doctorRepository.existsByCpf(cpf)) {
            throw new DataIntegrityViolationException(ApiMessages.CPF_UNIQUE_CONSTRAINT_VIOLATION);

        } else if (doctorRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException(ApiMessages.EMAIL_UNIQUE_CONSTRAINT_VIOLATION);
        }

        Doctor doctor = new Doctor();
        mapEntityFromDTO(doctor, doctorDTO);
        return doctorRepository.save(doctor).getDTO();
    }

    @Override
    public AttendanceDTO scheduleAttendance(AttendanceDTO attendanceDTO) {
        String patientCpf = ServiceUtils.removeCpfMask(attendanceDTO.getPatient().getCpf());
        Patient patient = patientService.findByCpf(patientCpf);

        // To get here the user has to be authenticated, so there's no way it doesn't exist.
        Doctor doctor = doctorRepository.findByEmail(getDoctorEmailFromAuthentication()).get();

        Attendance attendance = new Attendance(null, attendanceDTO.getLocalDateTime(), doctor, patient);
        return attendanceService.save(attendance).getDTO();
    }

    @Override
    public List<AttendanceDTO> findAttendances() {
        return attendanceService.findAllByDoctorEmail(getDoctorEmailFromAuthentication()).stream().map(Attendance::getDTO).collect(Collectors.toList());
    }
}
