package space.nixus.pubtrans.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
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
import space.nixus.pubtrans.error.RouteNotFound;
import space.nixus.pubtrans.model.Alert;
import space.nixus.pubtrans.model.AlertParam;
import space.nixus.pubtrans.model.FavoredRoute;
import space.nixus.pubtrans.model.Route;
import space.nixus.pubtrans.model.RouteQuery;
import space.nixus.pubtrans.repository.AlertRepository;
import space.nixus.pubtrans.repository.FavoredRepository;

@Service
public final class RouteService {

    private static final int MAX_RESULT_COUNT = 2;
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
            throw new RouteNotFound();
        }
        // get MAX_RESULT_COUNT routes
        int count = 0;
        for(var gRoute : result.routes) {
            // Create route with source and destination.
            var route = new Route(getSource(gRoute), getDestination(gRoute));
            // Get route steps.
            flattenRoute(route, gRoute);
            // Add result
            routes.add(route);
            if(++count > MAX_RESULT_COUNT) { break; }
        }
        return routes;
    }
    
    /**
     * Favor a route.
     * @param userId
     * @param source
     * @param destination
     * @return
     */
    public FavoredRoute favorRoute(Long userId, String source, String destination) {
        var fav = new FavoredRoute(null, userId, source, destination);
        return favoredRepository.save(fav);
    }

    /**
     * Remove a vavored route.
     * @param favoredId
     * @return
     */
    public boolean unfavorRoute(Long favoredId) {
        favoredRepository.deleteById(favoredId);
        return true;
    }

    /**
     * List favored.
     * @param userId
     * @return
     */
    public List<FavoredRoute> listFavored(Long userId) {
        return favoredRepository.findAllByUserId(userId);
    }


    /**
     * List registered alerts.
     * @return
     */
    public List<Alert> listAlerts() {
        var alerts = new ArrayList<Alert>();
        alertRepository.findAll().forEach(alert -> alerts.add(alert));
        return alerts;
    }

    /**
     * Add an alert.
     * @param param
     * @return
     */
    public Alert addAlert(AlertParam param) {
        var alert = new Alert(null, param.getDescription(), param.getExpires());
        return alertRepository.save(alert);
    }

    /**
     * Update an alert.
     * @param id
     * @param param
     * @return
     */
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
            0L);
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
        
        // transit steps
        addTransitSteps(route, steps.toArray(new DirectionsStep[steps.size()]), true);
        
        // private tail steps
        // skip if head walk end at destination (we didnt have transit step)
        if(tailWalkStart!=null && !(headWalkEnd!=null && headWalkEnd.equals(route.getDest().getLatLng()))) {
            LatLng end = gRoute.legs[gRoute.legs.length-1].endLocation;
            var results = walkingService.query(tailWalkStart, end);
            results.getPath().getSteps().forEach(s -> route.addStep(s));
            route.addDuration((long)(results.getPath().getEstimatedArrivalTime()*1000));
            route.setWeather(results.getWeather());
        }
        // update arrival time
        route.getDest().setTime(route.getSource().getTime() + route.getDuration());
    }

    /**
     * Render speps into route as string instructions.
     * @param route render target
     * @param steps steps to render
     * @param duration add duration (onloy adds parent step duration)
     */
    private void addTransitSteps(Route route, DirectionsStep[] steps, boolean duration) {
        for(var step : steps) {
            var td = step.transitDetails;
            if(td !=null) {
                route.addStep(String.format("At %s, board %s %s heading to %s.",
                    td.departureStop.name,
                    td.line.vehicle.name, td.line.shortName,
                    td.headsign));
                route.addStep(String.format("%d stops.",
                    td.numStops));
                route.addStep(String.format("Get off at stop %s.",
                    td.arrivalStop.name));
            } else {
                route.addStep(step.htmlInstructions);
            }
            if(duration) {
                route.addDuration(step.duration.inSeconds*1000);
            }
            if(step.steps!=null) {
                addTransitSteps(route, step.steps, false);
            }
        }
    }
}
