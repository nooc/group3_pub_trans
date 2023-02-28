package space.nixus.pubtrans.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.maps.model.LatLng;

import lombok.Getter;
import lombok.Setter;

@Service
public class WalkingDirectionsService {

    @Getter
    @Setter
    public static class Results {
        private List<String> steps;
    }

    @Value("${group3.services.walking.uri}")
    private String serviceUri;
    private WebClient client;

    public WalkingDirectionsService() {
        this.client = WebClient.builder()
            .baseUrl(serviceUri)
            .build();
    }

    public Results query(LatLng source, LatLng dest) {
        var res = new Results();
        // TODO Query external service.
        res.setSteps(List.of(
            "Walk till ya die."
        ));
        return res;
    }
}
