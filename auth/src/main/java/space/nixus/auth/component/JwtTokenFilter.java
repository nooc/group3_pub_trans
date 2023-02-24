package space.nixus.auth.component;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import space.nixus.auth.error.UnauthorizedError;
import space.nixus.auth.model.User;
import space.nixus.auth.repository.UserRepository;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Logger logger;
    @Autowired
    private Cryptic cryptic;
    @Value("${space.nixus.auth.issuer:space.nixus}")
    private String issuer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        // Get jwt token and validate
        var token = verifyAndDecode(header.substring(7).trim());
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        // Get user identity and set it on the spring security context
        User user = userRepository.findByEmail(token.getClaim("username").asString()).get(0);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private DecodedJWT verifyAndDecode(String token) throws UnauthorizedError {
        try {
            Algorithm algorithm = Algorithm.RSA256(cryptic.getPubKey(), cryptic.getPrivKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify an specific claim validations
                    .withIssuer(issuer)
                    // reusable verifier instance
                    .build();
            return verifier.verify(token);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }
}
