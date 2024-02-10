package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.exception.AttendanceException;
import br.com.conexasaude.challenge.model.Attendance;
import br.com.conexasaude.challenge.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AttendanceService {

    AttendanceRepository attendanceRepository;


    public Attendance save(Attendance attendance) {
        try {
            return attendanceRepository.save(attendance);
        } catch (DataIntegrityViolationException ex) {
            throw new AttendanceException(ApiMessages.ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION);
        }
    }

    public List<Attendance> findAllByDoctorEmail(String doctorEmail) {
        return attendanceRepository.findAllByDoctorEmail(doctorEmail);
    }
}
