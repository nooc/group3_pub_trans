package space.nixus.pubtrans.model;

import java.io.Serializable;
import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteQuery implements Serializable {
    
    @Unindexed
    private String source;
    @Unindexed
    private String destination;
}
