package com.project.ribbon.config;

import com.project.ribbon.filter.JwtAuthenticationFilter;
import com.project.ribbon.provide.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/sign").permitAll()
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
                .requestMatchers("/ribbon/admin/report").permitAll()
                .requestMatchers("/ribbon/admin/boardlogin").permitAll()
                .requestMatchers("/ribbon/admin/userlogin").permitAll()
                .requestMatchers("/ribbon/admin/commentslogin").permitAll()
                .requestMatchers("/ribbon/admin/post/commentslogin").permitAll()
                .requestMatchers("/ribbon/admin/post/userlogin").permitAll()
                .requestMatchers("/ribbon/admin/post/boardlogin").permitAll()
                .requestMatchers("/ribbon/admin/reportcomments").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportuser").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportboard").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/post/reportuserdelete").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}