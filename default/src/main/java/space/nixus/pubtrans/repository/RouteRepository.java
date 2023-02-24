package space.nixus.pubtrans.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.Route;

public interface RouteRepository extends DatastoreRepository<Route,Long> {
    
}
