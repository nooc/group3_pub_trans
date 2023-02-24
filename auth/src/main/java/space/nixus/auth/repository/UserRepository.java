package space.nixus.auth.repository;

import java.util.List;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.auth.model.User;


public interface UserRepository extends DatastoreRepository<User, Long> {

    List<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
