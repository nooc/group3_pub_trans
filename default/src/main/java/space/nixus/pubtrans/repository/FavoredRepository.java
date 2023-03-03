package space.nixus.pubtrans.repository;

import java.util.List;
import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import space.nixus.pubtrans.model.FavoredRoute;

/**
 * Google Datastore backed repository for FavoredRoute entities.
 */
public interface FavoredRepository extends DatastoreRepository<FavoredRoute,Long> {

    List<FavoredRoute> findAllByUserId(Long usderId);

    void deleteByUserId(Long userId);
}
