package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.model.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "jwt_log")
public class JwtLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(nullable = false)
    private String jwt;

    @Setter
    private Date revokedAt;

    @Column(nullable = false)
    private Date issuedAt;

    @Column(nullable = false)
    private Date expiration;
}
