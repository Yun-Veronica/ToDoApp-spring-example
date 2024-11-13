package org.example.todo.config;

import jakarta.annotation.PostConstruct;
import org.example.todo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Define the BCryptPasswordEncoder as a Bean (do not inject it through constructor)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain bean to configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity (you can enable it if required)
                .authorizeHttpRequests()
                    .requestMatchers("/registration").not().fullyAuthenticated()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/news").hasRole("USER")
                    .requestMatchers("/", "/resources/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/");
        return http.build();
    }

    // AuthenticationManager bean configuration
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserService userService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService) // Use your custom UserService for user authentication
                .passwordEncoder(passwordEncoder()); // Set the password encoder for BCrypt password hashing
        return authenticationManagerBuilder.build();
    }
}
