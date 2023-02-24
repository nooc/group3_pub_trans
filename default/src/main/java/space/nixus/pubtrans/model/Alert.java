package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "alert")
public class Alert extends AlertParam {
    @Id
    private Long id;

    /**
     * @param description
     * @param expires
     * @param routeId
     */
    public Alert(Long id, String description, Long expires, Long routeId) {
        super(description, expires, routeId);
        this.id = id;
    }

    /**
     * 
     */
    public Alert() {
        super();
        this.id = null;
    }
}
