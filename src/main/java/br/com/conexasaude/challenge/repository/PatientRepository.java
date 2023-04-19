package br.com.conexasaude.challenge.repository;

import br.com.conexasaude.challenge.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByCpf(String cpf);

    Optional<Patient> findByCpf(String cpf);
}
