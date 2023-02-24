package space.nixus.pubtrans.repository;

import java.util.List;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.Challenge;

public interface ChallengeRepository extends DatastoreRepository<Challenge,Long>{
    
    List<Challenge> findByValue(String value);

    void deleteByValue(String value);
}
