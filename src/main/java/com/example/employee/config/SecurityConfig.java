package com.example.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.employee.security.CustomUserDetailsService;
import com.example.employee.security.JwtAccessDeniedHandler;
import com.example.employee.security.JwtAuthenticationFilter;
import com.example.employee.security.OAuth2LoginSuccessHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

   private final JwtAuthenticationFilter jwtAuthenticationFilter;

   private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

   private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
   
   private final JwtAccessDeniedHandler jwtAccessDeniedHandler;



    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter,OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,AuthenticationEntryPoint jwtAuthenticationEntryPoint,JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
    
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**",
            "/oauth2/**",
                  "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
            ).permitAll()
            .requestMatchers(HttpMethod.GET,"/employees/**")
            .hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST,"/employees/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/employees/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/employees/**")
            .hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
        )
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
         .oauth2Login(oauth2 -> oauth2
            .successHandler(oAuth2LoginSuccessHandler)
         );
        return http.build();
    }
}
