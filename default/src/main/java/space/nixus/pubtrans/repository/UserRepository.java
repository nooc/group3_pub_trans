package space.nixus.pubtrans.repository;

import java.util.List;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.User;

/**
 * Google Datastore backed repository for User entities.
 */
public interface UserRepository extends DatastoreRepository<User, Long> {

    List<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
