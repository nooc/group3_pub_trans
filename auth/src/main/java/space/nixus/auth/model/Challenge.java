package space.nixus.auth.model;

import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;

@Getter
@Setter
public class Challenge extends ChallengeResponse {
    @Id
    private Long id;
    private Long userId;
    private Long expires;
    
    public Challenge() { super(); }
    public Challenge(Long userId, String plain, String encrypted, long expires) {
        super(plain, encrypted);
        this.id = null;
        this.userId = userId;
        this.expires = expires;
    }
}
