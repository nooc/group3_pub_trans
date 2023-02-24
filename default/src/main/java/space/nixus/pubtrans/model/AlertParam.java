package space.nixus.pubtrans.model;

import com.google.cloud.spring.data.datastore.core.mapping.Unindexed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlertParam {
    @Unindexed
    private String description;
    private Long expires;
    private Long routeId;
}
