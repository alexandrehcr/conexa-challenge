package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    DoctorDTO register(DoctorDTO doctorDTO);
    AttendanceDTO scheduleAttendance(AttendanceDTO attendance);
    List<AttendanceDTO> findAttendances();
}
