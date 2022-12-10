package com.backend.kmsproject.security;

import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.filter.CustomAuthenticationFilter;
import com.backend.kmsproject.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
        private final UserDetailsService userDetailsService;
        private final BCryptPasswordEncoder passwordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManagerBean(),
                                userDetailsService, passwordEncoder);
                filter.setFilterProcessesUrl("/api/login");
                http.csrf().disable();
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.authorizeRequests().antMatchers("/api/login/**").permitAll();
                http.authorizeRequests().antMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll();
                http.authorizeRequests().antMatchers("/api/customer/register/**").permitAll();
                http.authorizeRequests().antMatchers("/api/my-account/**").hasAnyAuthority(KmsRole.ADMIN_ROLE.getRole(),
                                KmsRole.FOOTBALL_PITCH_ROLE.getRole(), KmsRole.CUSTOMER_ROLE.getRole());
                http.authorizeRequests().antMatchers("/api/football-pitches/**")
                                .hasAuthority(KmsRole.ADMIN_ROLE.getRole());
                http.authorizeRequests().antMatchers("/api/sub-football-pitches/**")
                                .hasAuthority(KmsRole.FOOTBALL_PITCH_ROLE.getRole());
                http.authorizeRequests().antMatchers("/api/football-pitch-admins/**")
                                .hasAuthority(KmsRole.ADMIN_ROLE.getRole());
                http.authorizeRequests().antMatchers("/api/other-services/**")
                                .hasAuthority(KmsRole.FOOTBALL_PITCH_ROLE.getRole());
                http.authorizeRequests().anyRequest().authenticated();
                http.addFilter(filter);
                http.addFilterBefore(new CustomAuthorizationFilter(userDetailsService),
                                UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
        }
}
