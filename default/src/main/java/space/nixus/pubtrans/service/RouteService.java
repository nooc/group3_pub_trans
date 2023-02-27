package space.nixus.pubtrans.service;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DirectionsRoute;

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
            // Create route with source and destination.
            var route = new Route(getSource(gRoute), getDestination(gRoute));
            // Get route steps.
            processLegs(route, gRoute.legs);
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

    /**
     * Get source adress.
     * 
     * @param route
     * @return
     */
    private static String getSource(DirectionsRoute route) {
        return route.legs[0].startAddress;
    }

    /**
     * Get destination adress.
     * 
     * @param route
     * @return
     */
    private static String getDestination(DirectionsRoute route) {
        return route.legs[route.legs.length-1].endAddress;
    }

    private static void processLegs(Route route, DirectionsLeg[] legs) {
        for (var leg : legs) {
            processSteps(route, leg.steps);
        }
    }

    private static void processSteps(Route route, DirectionsStep[] steps) {
        for (var step : steps) {
            if(step.travelMode.equals(TravelMode.WALKING)) {
                if(queryWalk(route,step)) {
                    // Queried wakl
                    continue;
                }
            }
            route.addStep(step.duration.inSeconds * 1000, step.htmlInstructions);
        }
    }

    private static boolean queryWalk(Route route, DirectionsStep step) {
        // query external service
        return false;
    }
}
