package br.com.conexasaude.challenge.controller;

import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceDTO> scheduleAttendance(@Valid @RequestBody AttendanceDTO attendanceValidationDTO) {
        return ResponseEntity.ok(doctorService.scheduleAttendance(attendanceValidationDTO));
    }

    @GetMapping("/view-attendances")
    public ResponseEntity<List<AttendanceDTO>> getAttendances() {
        return ResponseEntity.ok(doctorService.findAttendances());
    }
}
