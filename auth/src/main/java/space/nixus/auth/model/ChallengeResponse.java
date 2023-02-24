package space.nixus.auth.model;

import lombok.Getter;
import lombok.Setter;
import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;

@Getter
@Setter
public class ChallengeResponse extends ChallengeParam {
    @Unindexed
    private String encrypted;
    
    public ChallengeResponse() { super(); }
    public ChallengeResponse(String plain, String encrypted) {
        super(plain);
        this.encrypted = encrypted;
    }
}
