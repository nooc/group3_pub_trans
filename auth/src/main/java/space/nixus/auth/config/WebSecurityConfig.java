package space.nixus.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import jakarta.servlet.http.HttpServletResponse;
import space.nixus.auth.component.JwtTokenFilter;
import space.nixus.auth.component.SimpleUserDetailsService;

// https://www.toptal.com/spring/spring-security-tutorial

@Configuration
public class WebSecurityConfig {
	
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            // Enable COR. Disable CSRF.
        return http.cors().and().csrf().disable()
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
            // Public endpoints
            .requestMatchers(
                "/auth/**",
                "/swagger-ui",
                "/swagger-ui/**",
                "/v3/**").permitAll()
            // Admin endpoints
            .requestMatchers("/users/**")
            .hasAnyRole("ADMIN")
            // Private endpoints
            .anyRequest().authenticated()
            .and()
            // JWT token filter
            .addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

	// Used by Spring Security if CORS is enabled.
    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        return new SimpleUserDetailsService();
    }
}
