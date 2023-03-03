package space.nixus.pubtrans.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import space.nixus.pubtrans.component.JwtTokenFilter;
import space.nixus.pubtrans.component.SimpleUserDetailsService;
import space.nixus.pubtrans.component.ServiceHeaderFilter;


/**
 * Security configurations.
 */
@Configuration
public class WebSecurityConfig {
	
    // Bearer token filter.
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    // Internal requests filter.
    @Autowired
    private ServiceHeaderFilter serviceHeaderFilter;
    
    /**
     * Set up HttpSecurity
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF.
        return http.csrf().disable()
            // Set session management to stateless
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Set permissions on endpoints
            .authorizeHttpRequests()
            // Public
            .requestMatchers("/swagger", "/swagger-ui","/swagger-ui/**", "/v3/**", "/auth/**")
            .permitAll()
            // Admin
            .requestMatchers("/users/**", "/internals/**")
            .hasAnyRole("ADMIN")
            // Private
            .anyRequest().authenticated()
            .and()
            // JWT token filter
            .addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            // Internal requests filter
            .addFilterBefore(serviceHeaderFilter, JwtTokenFilter.class)
            .build();
    }

    /**
     * Out simple user details.
     * @return
     */
    @Bean
    UserDetailsService getUserDetailsService() {
        return new SimpleUserDetailsService();
    }
}
