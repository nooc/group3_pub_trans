package space.nixus.pubtrans.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.Instant;
import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Route query response.
 */
@Getter
@Setter
public class Route implements Serializable {

    @AllArgsConstructor
    @Getter
    @Setter
    public static class AddressPoint implements Serializable {
        private String adress;
        private LatLng latLng;
        private Long time;
    }

    private Long created;  // EpochMilli
    private Long duration; // Millis
    private AddressPoint source;
    private AddressPoint dest;
    private List<String> steps;
    private String weather;

    /**
     * @param source
     * @param destination
     */
    public Route(AddressPoint source, AddressPoint dest) {
        this.created = Instant.now().toEpochMilli();
        this.duration = 0L;
        this.source = source;
        this.dest = dest;
        this.steps = new ArrayList<>();
    }

    public Route() {
        super();
        this.created = Instant.now().toEpochMilli();
        this.duration = 0L;
        this.steps = new ArrayList<>();
    }

    public void addStep(String description) {
        this.steps.add(description);
    }

    /*
     * Duration in millis.
     */
    public void addDuration(long duration) {
        this.duration += duration;
    }
}
