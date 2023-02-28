package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "favored")
public class FavoredRoute extends RouteQuery {

    @Id
    private Long id;
    private Long userId;
    private String source;
    private String destination;
}
