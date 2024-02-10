package br.com.conexasaude.challenge.controller;

import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
public class DoctorController {

    DoctorService doctorService;

    @PostMapping("/attendance")
    public ResponseEntity<AttendanceDTO> scheduleAttendance(@Valid @RequestBody AttendanceDTO attendanceValidationDTO) {
        return new ResponseEntity<>(doctorService.scheduleAttendance(attendanceValidationDTO), HttpStatus.CREATED);
    }
    
    @GetMapping("/view-attendances")
    public ResponseEntity<List<AttendanceDTO>> getAttendances() {
        return ResponseEntity.ok(doctorService.findAttendances());
    }
}
