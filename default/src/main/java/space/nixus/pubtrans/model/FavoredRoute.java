package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "favored")
public class FavoredRoute extends RouteQuery {

    @Id
    private Long id;
    @JsonIgnore
    private Long userId;
    
    /**
     * @param source
     * @param destination
     * @param id
     * @param userId
     */
    public FavoredRoute(Long id, Long userId, String source, String destination) {
        super(source, destination);
        this.id = id;
        this.userId = userId;
    }
 }
