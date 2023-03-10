package space.nixus.pubtrans.service;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Service for querying walking directions from external service.
 */
@Service
public final class WalkingDirectionsService {

    // models for remote service response

    @AllArgsConstructor
    public static final class Request {
        double startLatitude;
        double startLongitude;
        double endLatitude;
        double endLongitude;
        String owner;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Destination {
        long id;
        double startLongitude;
        double startLatitude;
        double endLongitude;
        double endLatitude;
        String owner;
        String movementType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Path {
        private Destination destination;
        private double estimatedArrivalTime;
        private List<String> steps;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Response {
        private Path path;
        private String weather;
    }
    
    @Autowired
    private Logger logger;
    @Value("${group3.services.walking.uri}")
    private String serviceUri;
    @Value("${WALKING_KEY}")
    private String serviceKey;
    private final WebClient client;

    /**
     * Construct creating web client.
     */
    public WalkingDirectionsService() {
        this.client = WebClient.builder()
            .baseUrl(serviceUri)
            .build();
    }

    /**
     * Query directions
     * @param source
     * @param dest
     * @return Response or null
     */
    public Response query(LatLng source, LatLng dest) {

        try {
            var responseEntity = client.post()
                .uri("/walk_path")
                .bodyValue(new Request(source.lat, source.lng, dest.lat, dest.lng, serviceKey))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Response.class).block();
            return responseEntity.getBody();
        } catch(Exception ex) {
            logger.error("Query to /walk_path", ex);
        }
        // Empty steps
        return new Response(new Path(
            new Destination(0, source.lng, source.lat, dest.lng, dest.lat, serviceKey, "walk"),
            0,
            List.of("Error")
        ),
        "Error");
    }
}
