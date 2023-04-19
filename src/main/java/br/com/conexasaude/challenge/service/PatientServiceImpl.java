package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.exception.BadRequestException;
import br.com.conexasaude.challenge.exception.EntityNotFoundException;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;


    @Override
    public Patient findByCpf(String cpf) {
        return patientRepository.findByCpf(cpf).orElseThrow(() -> {
             if (cpf.replaceAll("[.-]", "").length() == 11) {
                return new EntityNotFoundException(String.format(ApiMessages.PATIENT_NOT_FOUND, cpf));
            }
            return new BadRequestException(ApiMessages.INV_CPF);
        });
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return patientRepository.existsByCpf(cpf);
    }
}

