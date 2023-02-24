package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;
import com.google.cloud.Timestamp;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "route")
public class Route extends RouteQuery {

    @Id
    private Long id;
    private Timestamp created; 

    /**
     * @param source
     * @param destination
     */
    public Route(String source, String destination) {
        super(source, destination);
        this.id = null;
    }

    /**
     * 
     */
    public Route() {
        super();
        this.id = null;
    }
}
