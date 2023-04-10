package br.com.conexasaude.challenge.repository;

import br.com.conexasaude.challenge.model.dto.JwtLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface JwtLogRepository extends JpaRepository<JwtLog, Long> {

    @Query(value = "SELECT * FROM jwt_log WHERE doctor_id = ?1 AND revoked_at IS NULL", nativeQuery = true)
    Optional<JwtLog> findCurrentLogByUserId(Long userId);
}
