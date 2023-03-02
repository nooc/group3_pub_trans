package space.nixus.pubtrans.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import space.nixus.pubtrans.component.JwtTokenFilter;
import space.nixus.pubtrans.component.SimpleUserDetailsService;
import space.nixus.pubtrans.component.ServiceHeaderFilter;

// https://www.toptal.com/spring/spring-security-tutorial

@Configuration
public class WebSecurityConfig {
	
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private ServiceHeaderFilter serviceHeaderFilter;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF.
        return http.csrf().disable()
            // Set session management to stateless
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Unauthorized requests exception handler
            .exceptionHandling()
            .authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getMessage()
                    );
                })
            .and()
            // Set permissions on endpoints
            .authorizeHttpRequests()
            // Public
            .requestMatchers("/swagger", "/swagger-ui","/swagger-ui/**", "/v3/**", "/auth/**")
            .permitAll()
            // Admin
            .requestMatchers("/users/**", "/internals/**")
            .hasAnyRole("ADMIN")
            // Private endpoints
            .anyRequest().authenticated()
            .and()
            // JWT token filter
            .addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(serviceHeaderFilter, JwtTokenFilter.class)
            .build();
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        return new SimpleUserDetailsService();
    }
}
