package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import lombok.Getter;
import lombok.Setter;

/**
 * Authentication challenge.
 */
@Getter
@Setter
@Entity(name = "challenge")
public class Challenge extends ChallengeResponse {
    
    @Id
    private Long id;
    @Unindexed
    private Long userId;
    private Long expires; // epoc millis
    
    public Challenge() { super(); }
    public Challenge(Long id, Long userId, String plain, String encrypted, long expires) {
        super(plain, encrypted);
        this.id = id;
        this.userId = userId;
        this.expires = expires;
    }
}
