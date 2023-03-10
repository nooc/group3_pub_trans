package space.nixus.pubtrans.component;

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
import space.nixus.pubtrans.error.UnauthorizedError;
import space.nixus.pubtrans.model.User;
import space.nixus.pubtrans.repository.UserRepository;
import space.nixus.pubtrans.interfaces.ICryptic;

@Component
public final class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Logger logger;
    @Autowired
    private ICryptic cryptic;
    @Value("${space.nixus.pubtrans.issuer:space.nixus}")
    private String issuer;
    // An easy way to invalidate existing tokens by changing this value.
    @Value("${space.nixus.pubtrans.unique:space.nixus}")
    private String jwtUnique;

    /**
     * 
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header
        String headerContent = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Validate
        if (headerContent != null && headerContent.startsWith("Bearer ")) {
            // Get jwt token and validate
            try {
                var token = verifyAndDecode(headerContent.substring(7).trim());
                if (token != null) {
                    // Get user identity and set it on the spring security context
                    User user = userRepository.findByEmail(token.getClaim("username").asString()).get(0);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                            null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UnauthorizedError ex) {
                // invalid token
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 
     * @param jwt token
     * @return decoded token
     * @throws UnauthorizedError
     */
    private DecodedJWT verifyAndDecode(String jwt) throws UnauthorizedError {
        try {
            Algorithm algorithm = Algorithm.RSA256(cryptic.getPubKey(), cryptic.getPrivKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify an specific claim validations
                    .withIssuer(issuer)
                    .withClaim("unique", jwtUnique)
                    // reusable verifier instance
                    .build();
            return verifier.verify(jwt);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }
}
