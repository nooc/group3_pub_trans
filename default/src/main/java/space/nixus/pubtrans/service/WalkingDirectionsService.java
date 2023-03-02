package space.nixus.pubtrans.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Service
public class WalkingDirectionsService {

    @AllArgsConstructor
    class Request {
        double startLatitude;
        double startLongitude;
        double endLatitude;
        double endLongitude;
        String owner;
    }

    @Getter
    public class Destination {
        long id;
        double startLongitude;
        double startLatitude;
        double endLongitude;
        double endLatitude;
        String owner;
        String movementType;
    }

    @Getter
    public static class Path {
        private Destination destination;
        private double estimatedArrivalTime;
        private List<String> steps;
    }

    @Getter
    public static class Response {
        private Path path;
        private String weather;
    }

    @Value("${group3.services.walking.uri}")
    private String serviceUri;
    @Value("${group3.services.walking.key}")
    private String serviceKey;
    private WebClient client;

    public WalkingDirectionsService() {
        this.client = WebClient.builder()
            .baseUrl(serviceUri)
            .build();
    }

    public Response query(LatLng source, LatLng dest) {

        var responseEntity = client.post()
            .uri("/walk_path")
            .bodyValue(new Request(source.lat, source.lng, dest.lat, dest.lng, serviceKey))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(Response.class).block();

        return responseEntity.getBody();  
    }
}
