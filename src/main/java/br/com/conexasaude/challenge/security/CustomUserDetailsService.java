package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.constants.apimessages.ExceptionMessages;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException(ExceptionMessages.INVALID_USERNAME_PASSWORD));
        return User.builder()
                .username(doctor.getEmail())
                .password(doctor.getPwd())
                .roles("USER")
                .build();
    }
}
