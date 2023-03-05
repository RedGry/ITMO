package com.example.demo.configuration;

import com.example.demo.utils.sec.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@EnableWebSecurity
@Configuration
public class SecConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JWTUtil jwtUtil;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/api/user").permitAll()
                .antMatchers("/api/user/*").permitAll()
                .antMatchers("/api/point").permitAll()
                .antMatchers("/api/point/*").permitAll()
                .anyRequest().authenticated()
                .and().apply(new JWTConfig(jwtUtil));
    }

}
