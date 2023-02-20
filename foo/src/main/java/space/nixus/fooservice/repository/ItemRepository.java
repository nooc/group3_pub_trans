package space.nixus.fooservice.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.fooservice.model.Item;


public interface ItemRepository extends DatastoreRepository<Item, Long> {

}
