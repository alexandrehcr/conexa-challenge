package br.com.conexasaude.challenge.model;

import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    // `password` is a MySQL reserved keyword
    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String specialty;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Attendance> attendances;


    public DoctorDTO getDTO() {
        return createDTO(this);
    }

    public static DoctorDTO createDTO(Doctor doctor) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(doctor, DoctorDTO.class);
    }
}
