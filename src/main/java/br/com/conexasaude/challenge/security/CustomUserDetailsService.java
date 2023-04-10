package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException(ApiMessages.INVALID_USERNAME_PASSWORD));

        return User.builder()
                .username(doctor.getEmail())
                .password(doctor.getPwd())
                .roles("USER")
                .build();
    }
}
