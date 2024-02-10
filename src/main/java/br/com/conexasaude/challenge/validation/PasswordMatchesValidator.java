package br.com.conexasaude.challenge.validation;

import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        DoctorDTO doctor = (DoctorDTO) value;
        return doctor.getPwd().equals(doctor.getPwdConfirmation());
    }
}
