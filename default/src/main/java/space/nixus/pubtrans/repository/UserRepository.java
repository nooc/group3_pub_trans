package space.nixus.pubtrans.repository;

import java.util.List;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.User;


public interface UserRepository extends DatastoreRepository<User, Long> {

    List<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
