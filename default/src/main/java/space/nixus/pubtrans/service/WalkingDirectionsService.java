package space.nixus.pubtrans.service;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
public final class WalkingDirectionsService {

    private static final String DUMMY_RESP ="""
        {
            "path": {
                "destination": {
                    "id": 0,
                    "startLongitude": 12.034092,
                    "startLatitude": 57.689697,
                    "endLongitude": 12.016437,
                    "endLatitude": 57.700989,
                    "owner": "fatso",
                    "movementType": "walk"
                },
                "estimatedArrivalTime": 1250.6,
                "steps": [
                    "After 63.0m Head northwest",
                    "After 530.6m Keep left onto Storatorpsvägen",
                    "After 728.0m Turn slight right onto Storatorpsvägen",
                    "After 346.3m Keep left",
                    "After 32.4m Turn right",
                    "After 36.5m Turn left",
                    "After 0.0m Arrive at your destination, on the left"
                ]
            },
            "weather": "Fog"
        }""";

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
    @Value("${group3.services.walking.key}")
    private String serviceKey;
    private final WebClient client;

    public WalkingDirectionsService() {
        this.client = WebClient.builder()
            .baseUrl(serviceUri)
            .build();
    }

    public Response query(LatLng source, LatLng dest) {

        // TODO enable when masadan is ready
        /*
        var responseEntity = client.post()
            .uri("/walk_path")
            .bodyValue(new Request(source.lat, source.lng, dest.lat, dest.lng, serviceKey))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(Response.class).block();

        return responseEntity.getBody();
        */
        var mapper = new ObjectMapper();
        try {
            return mapper.readValue(DUMMY_RESP, Response.class);
        } catch (Exception ex) {
            logger.error("WalkingDirectionsService", ex);
        }

        // Error steps
        return new Response(new Path(
            new Destination(0, 0, 0, 0, 0, serviceKey, "walk"),
            0,
            List.of("error")
        ),
        "error");
    }
}
