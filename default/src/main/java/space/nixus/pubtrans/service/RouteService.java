package space.nixus.pubtrans.service;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;

import space.nixus.pubtrans.model.Alert;
import space.nixus.pubtrans.model.AlertParam;
import space.nixus.pubtrans.model.Route;
import space.nixus.pubtrans.model.RouteQuery;
import space.nixus.pubtrans.repository.AlertRepository;
import space.nixus.pubtrans.repository.RouteRepository;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;
    @Autowired
    AlertRepository alertRepository;
    @Autowired
    GeoApiContext geoApiContext;

    /**
     * Query routes from Google DirectionsApi.
     * 
     * @param query
     * @return List of routes.
     */
    public List<Route> queryRoute(RouteQuery query) {
        List<Route> routes = new ArrayList<>();
        // Query
        var result = DirectionsApi.getDirections(
            geoApiContext,
            query.getSource(), 
            query.getDestination())
            .alternatives(true)
            .departureTimeNow()
            .mode(TravelMode.TRANSIT)
            .awaitIgnoreError();
        if(result==null) {
            throw new InternalError();
        }
        for(var gRoute : result.routes) {
            var route = new Route();
            // TODO: Get start and stop

            // TODO: process route steps.
            // if first position is not transit station, use external service to find route from start to first station
            // and do the same on the destination side.

            // Add result
            routes.add(route);
        }
        return routes;
    }
    
    public boolean favorRoute(Long userId, Long routeId) {
        throw new NotImplementedException();
    }

    public boolean unfavorRoute(Long routeId) {
        throw new NotImplementedException();
    }

    public List<Alert> listAlerts() {
        throw new NotImplementedException();
    }

    public Alert addAlert(AlertParam param) {
        throw new NotImplementedException();
    }

    
    public Alert updateAlert(Long id, AlertParam param) {
        throw new NotImplementedException();
    }

    public boolean deleteAlert(Long alertId) {
        throw new NotImplementedException();
    }
}
