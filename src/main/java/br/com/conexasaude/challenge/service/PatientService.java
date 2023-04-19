package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Patient;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {
    Patient findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
