package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Attendance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {
    Attendance save(Attendance attendance);
    List<Attendance> findAllByDoctorEmail(String doctorEmail);
}
