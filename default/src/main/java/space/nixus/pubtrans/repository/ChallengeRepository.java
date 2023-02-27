package space.nixus.pubtrans.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.google.cloud.datastore.Key;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import com.google.cloud.spring.data.datastore.repository.query.Query;

import space.nixus.pubtrans.model.Challenge;

public interface ChallengeRepository extends DatastoreRepository<Challenge,Long>{
    
    List<Challenge> findByValue(String value);

    void deleteByValue(String value);

    @Query("SELECT __key__ FROM challenge WHERE expires < @now")
    List<Key> findExpired(@Param("now") Long now);
}
