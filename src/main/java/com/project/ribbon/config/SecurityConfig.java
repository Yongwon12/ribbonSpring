package com.project.ribbon.config;

import com.project.ribbon.filter.JwtAuthenticationFilter;
import com.project.ribbon.provide.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/sign").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/upload").permitAll()
                .requestMatchers("/api/boardimage/**").permitAll()
                .requestMatchers("/api/userimage/**").permitAll()
                .requestMatchers("/api/groupimage/**").permitAll()
                .requestMatchers("/api/usedimage/**").permitAll()
                .requestMatchers("/api/realtimeup").hasRole("USER")
                .requestMatchers("/api/board").hasRole("USER")
                .requestMatchers("/api/individual").hasRole("USER")
                .requestMatchers("/api/group").hasRole("USER")
                .requestMatchers("/api/used").hasRole("USER")
                .requestMatchers("/api/post/**").hasRole("USER")
                .requestMatchers("/api/realtimeup").hasRole("ADMIN")
                .requestMatchers("/api/board").hasRole("ADMIN")
                .requestMatchers("/api/individual").hasRole("ADMIN")
                .requestMatchers("/api/group").hasRole("ADMIN")
                .requestMatchers("/api/used").hasRole("ADMIN")
                .requestMatchers("/api/post/**").hasRole("ADMIN")
                .requestMatchers("/api/post/**").hasRole("ADMIN")
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}