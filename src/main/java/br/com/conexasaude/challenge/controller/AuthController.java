package br.com.conexasaude.challenge.controller;

import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
public class AuthController {

    private DoctorService doctorService;

    @PostMapping(path = "/signup")
    public ResponseEntity<DoctorDTO> signup(@Valid @RequestBody DoctorDTO doctorValidationDTO) {
        DoctorDTO doctorDTO = doctorService.register(doctorValidationDTO);
        return new ResponseEntity<>(doctorDTO, HttpStatus.CREATED);
    }
}
