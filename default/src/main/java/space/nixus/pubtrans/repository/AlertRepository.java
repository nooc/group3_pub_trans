package space.nixus.pubtrans.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.Alert;

public interface AlertRepository extends DatastoreRepository<Alert,Long> {
    
}
