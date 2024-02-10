package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.security.filter.CustomAuthenticationFilter;
import br.com.conexasaude.challenge.security.filter.ExceptionHandlerFilter;
import br.com.conexasaude.challenge.security.filter.JwtValidatorFilter;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static br.com.conexasaude.challenge.constants.SecurityConstants.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtLogService jwtLogService;
    private JwtService jwtService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(daoAuthenticationProvider(), jwtService, jwtLogService);
        authenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(REGISTRATION_PATH, LOGIN_PATH, HOME_PATH).permitAll()
                        .requestMatchers("/**").authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logoutRequest -> logoutRequest.logoutUrl(LOGOUT_PATH)
                        .addLogoutHandler(new CustomLogoutHandler(jwtService, jwtLogService))
                        .logoutSuccessUrl(HOME_PATH)
                        .logoutSuccessHandler((request, response, authentication)
                                -> response.setStatus(HttpServletResponse.SC_OK)))
                .addFilterBefore(new ExceptionHandlerFilter(), ChannelProcessingFilter.class)
                .addFilter(authenticationFilter)
                .addFilterBefore(new JwtValidatorFilter(jwtService, jwtLogService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
