package space.nixus.pubtrans.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import space.nixus.pubtrans.repository.ChallengeRepository;
import space.nixus.pubtrans.service.UserService;
import space.nixus.pubtrans.interfaces.ICryptic;
import space.nixus.pubtrans.error.InternalError;
import space.nixus.pubtrans.error.ExpiredError;
import space.nixus.pubtrans.error.UnauthorizedError;
import space.nixus.pubtrans.error.UserNotFoundError;
import space.nixus.pubtrans.model.Challenge;
import space.nixus.pubtrans.model.ChallengeParam;
import space.nixus.pubtrans.model.ChallengeResponse;
import space.nixus.pubtrans.model.AuthRequest;
import space.nixus.pubtrans.model.TokenResponse;
import space.nixus.pubtrans.model.User;

/**
 * Authentication controller for handling the challenge authentication flow.
 * See /swagger
 */
@RestController
public final class AuthController {

    @Autowired
    private Logger logger;
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ICryptic cryptic;
    @Value("${space.nixus.pubtrans.challenge-minutes:10}")
    private int challengeMinutes;
    @Value("${space.nixus.pubtrans.issuer:space.nixus}")
    private String issuer;
    // An easy way to invalidate existing tokens by changing this value.
    @Value("${space.nixus.pubtrans.unique:space.nixus}")
    private String jwtUnique; 
    @Value("${space.nixus.pubtrans.expiry-minutes:10}")
    private long expiryMinutes;
    private Algorithm algorithm = null;
    
    /**
     * Request challenge.
     * See /swagger
     * @param request
     * @return
     */
    @PostMapping("/auth/request")
    ChallengeParam requestChallenge(@RequestBody AuthRequest request) {
        User user = userService.findByUsername(request.getUser());
        var now = Instant.now();
        if(user == null) {
            throw new UserNotFoundError();
        }
        try {
            // Delete expired
            // TODO move to internals
            var keys = challengeRepository.findExpired(now.toEpochMilli());
            var ids = new ArrayList<Long>();
            keys.stream().forEach( key -> ids.add(key.getId()) );
            challengeRepository.deleteAllById(ids);
            // Create challenge
            var plain = UUID.randomUUID().toString();
            var crypted = cryptic.encrypt(plain);
            var expires = now.plusSeconds(challengeMinutes * 60).toEpochMilli();
            var challenge = new Challenge(null, user.getId(), plain, crypted, expires);
            // store
            challenge = challengeRepository.save(challenge);
            // return challenge value
            return new ChallengeParam(challenge.getValue());
        } catch(Exception ex) {
            logger.error("requestChallenge", ex);
            throw new InternalError();
        }
    }

    /**
     * Validate auth challenge response.
     * See /swagger
     * @param response
     * @return
     */
    @PostMapping("/auth/validate")
    TokenResponse validateChallenge(@RequestBody ChallengeResponse response) {
        try {
            // find challenge
            var challenges = challengeRepository.findByValue(response.getValue());
            if(challenges.size() != 0) {
                var challenge = challenges.get(0);
                // check response.decrypted against challenge.value
                if(cryptic.decrypt(response.getEncrypted()).equals(challenge.getValue())) {
                    // Expired?
                    if(Instant.ofEpochMilli(challenge.getExpires()).isBefore(Instant.now())) {
                        throw new ExpiredError();
                    }
                    // Get user
                    User user = userService.findById(challenge.getUserId());
                    if(user!=null) {
                        // remove challenge
                        challengeRepository.delete(challenge);
                        // return new token
                        return new TokenResponse(
                            JWT.create()
                            .withHeader(Map.of(
                                "alg","RS256",
                                "typ","JWT"
                            ))
                            .withClaim("username", user.getEmail())
                            .withClaim("unique", jwtUnique)
                            .withIssuer(issuer)
                            .withExpiresAt(Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES))
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

    /**
     * Get algorith instance for sign and verify. 
     * @return
     */
    private Algorithm getAlgorithm() {
        if(algorithm == null) {
            algorithm = Algorithm.RSA256(cryptic.getPubKey(), cryptic.getPrivKey());
        }
        return algorithm;
    }
}
