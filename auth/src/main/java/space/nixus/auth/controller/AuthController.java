package space.nixus.auth.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import space.nixus.auth.repository.ChallengeRepository;
import space.nixus.auth.service.UserService;
import space.nixus.auth.component.Cryptic;
import space.nixus.auth.error.UnauthorizedError;
import space.nixus.auth.error.UserNotFoundError;
import space.nixus.auth.model.Challenge;
import space.nixus.auth.model.ChallengeParam;
import space.nixus.auth.model.ChallengeResponse;
import space.nixus.auth.model.RequestUser;
import space.nixus.auth.model.TokenResponse;
import space.nixus.auth.model.User;

@RestController
public class AuthController {

    @Autowired
    private Logger logger;
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private Cryptic cryptic;
    @Value( "${space.nixus.auth.challengeminutes:2}")
    private int challengeMinutes;
    @Value("${space.nixus.auth.issuer:space.nixus}")
    private String issuer;
    @Value("${space.nixus.auth.expiryMinutes:120}")
    private long expiryMinutes;
    private Algorithm algorithm = null;
    
    @PostMapping("/auth/request")
    ChallengeParam requestChallenge(@RequestBody RequestUser request) {
        User user = userService.findByUsername(request.getUser());
        if(user == null) {
            throw new UserNotFoundError();
        }
        try {
            var plain = UUID.randomUUID().toString();
            var crypted = cryptic.encryptToString(plain);
            var expires = Instant.now().plus(challengeMinutes, ChronoUnit.MINUTES).toEpochMilli();
            var challenge = new Challenge(user.getId(), plain, crypted, expires);
            return challengeRepository.save(challenge);
        } catch(Exception ex) {
            logger.error("requestChallenge", ex);
        }
        throw new InternalError();
    }

    @PostMapping("/auth/validate")
    TokenResponse validateChallenge(@RequestBody ChallengeResponse response) {
        try {
            if(response.getEncrypted().equals(cryptic.decryptToString(response.getValue()))) {
                var challenge = challengeRepository.findByValue(response.getValue());
                if(challenge != null) {
                    User user = userService.findById(challenge.getUserId());
                    if(user!=null) {
                        challengeRepository.delete(challenge);
                        return new TokenResponse(
                            JWT.create()
                            .withClaim("username", user.getEmail())
                            .withIssuer(issuer)
                            .withExpiresAt(Instant.now().plusSeconds(expiryMinutes))
                            .sign(getAlgorithm())
                            );
                    }
                }
            }
            throw new UnauthorizedError();
        } catch(Exception ex) {
            logger.error("requestChallenge", ex);
        }
        throw new InternalError();
    }

    private Algorithm getAlgorithm() {
        if(algorithm == null) {
            algorithm = Algorithm.RSA256(cryptic.getPubKey(), cryptic.getPrivKey());
        }
        return algorithm;
    }
}
