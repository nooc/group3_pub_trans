package space.nixus.pubtrans.model;

import java.util.List;
import java.util.ArrayList;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "route")
public class Route extends RouteQuery {

    @Id
    private Long id;
    private Long created;  // EpochMilli
    private Long duration; // Millis
    private List<String> steps;

    /**
     * @param source
     * @param destination
     */
    public Route(String source, String destination) {
        super(source, destination);
        this.id = null;
        this.created = Instant.now().toEpochMilli();
        this.duration = 0L;
        this.steps = new ArrayList<>();
    }

    /**
     * 
     */
    public Route() {
        super();
        this.id = null;
        this.created = Instant.now().toEpochMilli();
        this.duration = 0L;
        this.steps = new ArrayList<>();
    }

    public void addStep(long duration, String description) {
        this.duration += duration;
        this.steps.add(description);
    }
}
