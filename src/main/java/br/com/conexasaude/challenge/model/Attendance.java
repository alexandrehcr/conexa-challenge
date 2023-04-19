package br.com.conexasaude.challenge.model;

import br.com.conexasaude.challenge.constants.JsonConstants;
import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "date_time"}),
        @UniqueConstraint(columnNames = {"patient_id", "date_time"})
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(value = JsonConstants.VALUE_LOCAL_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonConstants.PATTERN_LOCAL_DATE_TIME)
    @Column(name = "date_time", nullable = false)
    private LocalDateTime localDateTime;

    @ManyToOne(optional = false)
    @JsonProperty(value = JsonConstants.VALUE_DOCTOR)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne(optional = false)
    @JsonProperty(value = JsonConstants.VALUE_PATIENT)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;


    public AttendanceDTO getDTO() {
        return createDTO(this);
    }

    public static AttendanceDTO createDTO(Attendance attendance) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(attendance, AttendanceDTO.class);
    }
}


