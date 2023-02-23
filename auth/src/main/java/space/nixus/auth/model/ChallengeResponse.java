package space.nixus.auth.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeResponse extends ChallengeParam {
    private String encrypted;
    
    public ChallengeResponse() { super(); }
    public ChallengeResponse(String plain, String encrypted) {
        super(plain);
        this.encrypted = encrypted;
    }
}
