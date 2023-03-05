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
                .requestMatchers("/api/post/updateinstructoruser").permitAll()
                .requestMatchers("/api/boardimage/**").permitAll()
                .requestMatchers("/api/userimage/**").permitAll()
                .requestMatchers("/api/groupimage/**").permitAll()
                .requestMatchers("/api/usedimage/**").permitAll()
                .requestMatchers("/api/realtimeup").hasRole("USER")
                .requestMatchers("/api/board").hasRole("USER")
                //.requestMatchers("/api/board").hasRole("INSTRUCTOR")
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
                .requestMatchers("/ribbon").permitAll()
                .requestMatchers("/ribbon/ribbon.png").permitAll()
                .requestMatchers("/ribbon/admin/reportuser").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportboard").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportindividual").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportgroup").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportused").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportcomments").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportindividualcomments").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportgroupcomments").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportusedcomments").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/announcement").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/postinsertannouncement").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/insertannouncement").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/announcementinfo").permitAll()
                .requestMatchers("/ribbon/admin/post/**").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/adminannouncementinfo").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertuser").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertboard").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertindividual").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertgroup").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertused").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertcomments").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertindividualcomments").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertgroupcomments").hasAnyRole("USER","ADMIN")
                .requestMatchers("/ribbon/admin/reportinsertusedcomments").hasAnyRole("USER","ADMIN")
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