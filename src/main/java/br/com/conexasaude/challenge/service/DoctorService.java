package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.model.Attendance;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.conexasaude.challenge.util.RemoveMask.removeCpfMask;
import static br.com.conexasaude.challenge.util.RemoveMask.removePhoneMask;

@AllArgsConstructor
@Service
public class DoctorService {

    private PasswordEncoder passwordEncoder;
    private DoctorRepository doctorRepository;
    private AttendanceService attendanceService;
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

    public DoctorDTO register(DoctorDTO doctorDTO) {
        String cpf = removeCpfMask(doctorDTO.getCpf());
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

    public AttendanceDTO scheduleAttendance(AttendanceDTO attendanceDTO) {
        String patientCpf = removeCpfMask(attendanceDTO.getPatient().getCpf());
        Patient patient = patientService.findByCpf(patientCpf);

        // To get here the user has to be authenticated, so there's no way it doesn't exist.
        Doctor doctor = doctorRepository.findByEmail(getDoctorEmailFromAuthentication()).get();

        Attendance attendance = new Attendance(null, attendanceDTO.getLocalDateTime(), doctor, patient);
        return attendanceService.save(attendance).getDTO();
    }

    public List<AttendanceDTO> findAttendances() {
        return attendanceService.findAllByDoctorEmail(getDoctorEmailFromAuthentication()).stream().map(Attendance::getDTO).collect(Collectors.toList());
    }
}
