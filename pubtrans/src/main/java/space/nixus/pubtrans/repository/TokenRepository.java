package space.nixus.pubtrans.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.Token;


public interface TokenRepository extends DatastoreRepository<Token, Long> {

    Token findByValue(String value);
}
