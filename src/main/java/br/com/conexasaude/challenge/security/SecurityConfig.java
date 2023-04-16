package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.security.filter.CustomAuthenticationFilter;
import br.com.conexasaude.challenge.security.filter.ExceptionHandlerFilter;
import br.com.conexasaude.challenge.security.filter.JwtValidatorFilter;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.service.JwtLogServiceImpl;
import br.com.conexasaude.challenge.util.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static br.com.conexasaude.challenge.constants.SecurityConstants.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(daoAuthenticationProvider(), jwtUtils(), jwtLogService());
        authenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().authorizeHttpRequests().requestMatchers(PathRequest.toH2Console()).permitAll()
                .and().authorizeHttpRequests(httpRequest -> httpRequest
                        .requestMatchers(REGISTRATION_PATH, LOGIN_PATH, HOME_PATH).permitAll()
                        .requestMatchers("/**").authenticated())
                .formLogin().disable()
                .logout(logoutRequest -> logoutRequest.logoutUrl(LOGOUT_PATH)
                        .addLogoutHandler(logoutHandler())
                        .logoutSuccessUrl(HOME_PATH)
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK)))
                .addFilterBefore(new ExceptionHandlerFilter(), ChannelProcessingFilter.class)
                .addFilter(authenticationFilter)
                .addFilterBefore(new JwtValidatorFilter(jwtUtils(), jwtLogService()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public JwtLogService jwtLogService() {
        return new JwtLogServiceImpl();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new CustomLogoutHandler(jwtUtils(), jwtLogService());
    }
}
