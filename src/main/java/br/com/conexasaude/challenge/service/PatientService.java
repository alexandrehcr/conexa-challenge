package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.apimessages.ValidationConstants;
import br.com.conexasaude.challenge.exception.BadRequestException;
import br.com.conexasaude.challenge.exception.EntityNotFoundException;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.conexasaude.challenge.constants.apimessages.ExceptionMessages.PATIENT_NOT_FOUND;


@AllArgsConstructor
@Service
public class PatientService {

    PatientRepository patientRepository;


    public Patient findByCpf(String cpf) {
        return patientRepository.findByCpf(cpf).orElseThrow(() -> {
             if (cpf.replaceAll("[.-]", "").length() == 11) {
                return new EntityNotFoundException(String.format(PATIENT_NOT_FOUND, cpf));
            }
            return new BadRequestException(ValidationConstants.INV_CPF);
        });
    }

    public boolean existsByCpf(String cpf) {
        return patientRepository.existsByCpf(cpf);
    }
}
