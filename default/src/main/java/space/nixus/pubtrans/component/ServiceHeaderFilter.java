package space.nixus.pubtrans.component;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.nixus.pubtrans.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Value;
import space.nixus.pubtrans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Authorize requests with the header.
 */
@Component
public final class ServiceHeaderFilter extends OncePerRequestFilter {

    private static final String[] HEADERS = {"X-Appengine-Cron"};

    @Autowired
    private UserRepository userRepository;
    @Value("${ADMIN_USER}")
    private String adminUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // find any of HEADERS
        for(var header : HEADERS) {
            var headerContent = request.getHeader(header);
            if(headerContent != null) {
                // Get admin identity and set it on the spring security context
                User user = userRepository.findByEmail(adminUser).get(0);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }
        }
        filterChain.doFilter(request, response);
    }
}
