package space.nixus.pubtrans.service;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.Instant;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;


import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;

import space.nixus.pubtrans.error.AlertNotFoundError;
import space.nixus.pubtrans.model.Alert;
import space.nixus.pubtrans.model.AlertParam;
import space.nixus.pubtrans.model.FavoredRoute;
import space.nixus.pubtrans.model.Route;
import space.nixus.pubtrans.model.RouteQuery;
import space.nixus.pubtrans.repository.AlertRepository;
import space.nixus.pubtrans.repository.FavoredRepository;

@Service
public class RouteService {

    @Autowired
    AlertRepository alertRepository;
    @Autowired
    FavoredRepository favoredRepository;
    @Autowired
    GeoApiContext geoApiContext;
    @Autowired
    WalkingDirectionsService walkingService;

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
            flattenRoute(route, gRoute);
            
            // Add result
            routes.add(route);
        }
        return routes;
    }
    
    public FavoredRoute favorRoute(Long userId, String source, String destination) {
        var fav = new FavoredRoute(null, userId, source, destination);
        return favoredRepository.save(fav);
    }

    public boolean unfavorRoute(Long favoredId) {
        favoredRepository.deleteById(favoredId);
        return true;
    }

    public List<FavoredRoute> listFavored(Long userId) {
        return favoredRepository.findAllByUserId(userId);
    }


    public List<Alert> listAlerts() {
        var alerts = new ArrayList<Alert>();
        alertRepository.findAll().forEach(alert -> alerts.add(alert));
        return alerts;
    }

    public Alert addAlert(AlertParam param) {
        var alert = new Alert(null, param.getDescription(), param.getExpires());
        return alertRepository.save(alert);
    }

    
    public Alert updateAlert(Long id, AlertParam param) {
        var alertOpt = alertRepository.findById(id);
        if(!alertOpt.isPresent()) {
            throw new AlertNotFoundError();
        }
        var alert = alertOpt.get();
        if(param.getDescription()!= null) { alert.setDescription(param.getDescription()); }
        if(param.getExpires()!= null) { alert.setExpires(param.getExpires()); }
        return alertRepository.save(alert);
    }

    public boolean deleteAlert(Long alertId) {
        alertRepository.deleteById(alertId);
        return true;
    }

    /**
     * Get source adress.
     * 
     * @param route
     * @return
     */
    private static Route.AddressPoint getSource(DirectionsRoute route) {
        return new Route.AddressPoint(route.legs[0].startAddress,
            route.legs[0].startLocation,
            route.legs[0].departureTime.toEpochSecond()*1000);
    }

    /**
     * Get destination adress.
     * 
     * @param route
     * @return
     */
    private static Route.AddressPoint getDestination(DirectionsRoute route) {
        var index = route.legs.length-1;
        return new Route.AddressPoint(route.legs[index].endAddress,
            route.legs[index].endLocation,
            route.legs[0].arrivalTime.toEpochSecond()*1000);
    }

    /**
     * Get all steps from google route.
     * Add walking steps from WalkingDirections service.
     * 
     * @param route
     * @param gRoute
     */
    private void flattenRoute(Route route, DirectionsRoute gRoute) {
        LinkedList<DirectionsStep> steps = new LinkedList<>();
        // flatten
        for(var leg : gRoute.legs) {
            for(var step : leg.steps) {
                steps.add(step);
            }
        }
        DirectionsStep dstep;
        // strip head walks
        LatLng headWalkEnd = null;
        while(!steps.isEmpty() && (dstep=steps.peekFirst()).travelMode.equals(TravelMode.WALKING)) {
            steps.removeFirst();
            headWalkEnd = dstep.endLocation;
        }
        // strip tail walks
        LatLng tailWalkStart = null;
        while(!steps.isEmpty() && (dstep=steps.peekLast()).travelMode.equals(TravelMode.WALKING)) {
            steps.removeLast();
            tailWalkStart = dstep.startLocation;
        }

        // private head steps
        if(headWalkEnd!= null) {
            LatLng start = gRoute.legs[0].startLocation;
            var results = walkingService.query(start, headWalkEnd);
            results.getPath().getSteps().forEach(s -> route.addStep(s));
            route.addDuration((long)(results.getPath().getEstimatedArrivalTime()*1000));
            route.setWeather(results.getWeather());
        }
        
        // google steps
        for(var step : steps) {
            route.addStep(step.htmlInstructions);
            route.addDuration(step.duration.inSeconds*1000);
        }
        // private tail steps
        if(tailWalkStart!=null) {
            LatLng end = gRoute.legs[gRoute.legs.length-1].endLocation;
            var results = walkingService.query(tailWalkStart, end);
            results.getPath().getSteps().forEach(s -> route.addStep(s));
            route.addDuration((long)(results.getPath().getEstimatedArrivalTime()*1000));
            route.setWeather(results.getWeather());
        }
    }
}
