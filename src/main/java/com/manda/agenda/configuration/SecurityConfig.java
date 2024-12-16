package com.manda.agenda.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.manda.agenda.handlers.CustomAuthenticationHandle;
import com.manda.agenda.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/public/**").permitAll()

                .requestMatchers("/error").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/first/**").hasRole("FIRST_LOGIN")
                .requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated())
                .formLogin(
                        form -> form.loginPage("/login").loginProcessingUrl("/connect")
                                .successHandler(new CustomAuthenticationHandle())
                                // .defaultSuccessUrl("/listeEvenement", true)
                                .permitAll())

                .logout(logout -> logout.permitAll()).userDetailsService(userService);
        // .csrf().disable().headers().frameOptions().disable()
        return http.build();
    }

    // Si le mot de passe n'est pas crypté dans la base de données, celui-ci n'est
    // pas recommendé
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return NoOpPasswordEncoder.getInstance();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
