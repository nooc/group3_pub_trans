package space.nixus.pubtrans.controller;

import java.time.Instant;
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
import space.nixus.pubtrans.component.Cryptic;
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
    @Value("${space.nixus.pubtrans.challenge-minutes:10}")
    private int challengeMinutes;
    @Value("${space.nixus.pubtrans.issuer:space.nixus}")
    private String issuer;
    @Value("${space.nixus.pubtrans.expiry-minutes:10}")
    private long expiryMinutes;
    private Algorithm algorithm = null;
    
    @PostMapping("/auth/request")
    ChallengeParam requestChallenge(@RequestBody AuthRequest request) {
        User user = userService.findByUsername(request.getUser());
        var now = Instant.now();
        if(user == null) {
            throw new UserNotFoundError();
        }
        try {
            // Delete expired.
            var keys = challengeRepository.findExpired(now.toEpochMilli());
            var ids = new ArrayList<Long>();
            keys.stream().forEach( key -> ids.add(key.getId()) );
            challengeRepository.deleteAllById(ids);

            var plain = UUID.randomUUID().toString();
            var crypted = cryptic.encrypt(plain);
            var expires = now.plusSeconds(challengeMinutes * 60).toEpochMilli();
            var challenge = new Challenge(null, user.getId(), plain, crypted, expires);
            challenge = challengeRepository.save(challenge);
            return new ChallengeParam(challenge.getValue());
        } catch(Exception ex) {
            logger.error("requestChallenge", ex);
            throw new InternalError();
        }
    }

    @PostMapping("/auth/validate")
    TokenResponse validateChallenge(@RequestBody ChallengeResponse response) {
        try {
            if(response.getValue().equals(cryptic.decrypt(response.getEncrypted()))) {
                var challenges = challengeRepository.findByValue(response.getValue());
                if(challenges.size() != 0) {
                    var challenge = challenges.get(0);
                    // Expired?
                    if(Instant.ofEpochMilli(challenge.getExpires()).isBefore(Instant.now())) {
                        throw new ExpiredError();
                    }
                    User user = userService.findById(challenge.getUserId());
                    if(user!=null) {
                        challengeRepository.delete(challenge);
                        return new TokenResponse(
                            JWT.create()
                            .withHeader(Map.of(
                                "alg","RS256",
                                "typ","JWT"
                            ))
                            .withClaim("username", user.getEmail())
                            .withIssuer(issuer)
                            .withExpiresAt(Instant.now().plusSeconds(expiryMinutes*60))
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
