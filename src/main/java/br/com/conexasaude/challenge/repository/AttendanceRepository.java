package br.com.conexasaude.challenge.repository;

import br.com.conexasaude.challenge.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByDoctorEmail(String email);
}
