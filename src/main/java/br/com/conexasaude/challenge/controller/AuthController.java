package br.com.conexasaude.challenge.controller;

import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1")
public class AuthController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping(path = "/signup")
    public ResponseEntity<DoctorDTO> signup(@Valid @RequestBody DoctorDTO doctorValidationDTO) {
        DoctorDTO doctorDTO = doctorService.register(doctorValidationDTO);
        URI uri = ServletUriComponentsBuilder.fromPath("api/v1/doctors").path("/{id}").buildAndExpand(doctorDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(doctorDTO);
    }
}
