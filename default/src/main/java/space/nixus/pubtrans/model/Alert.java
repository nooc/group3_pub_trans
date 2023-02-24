package space.nixus.pubtrans.model;

import org.springframework.data.annotation.Id;

public class Alert extends AlertParam {
    @Id
    private Long id;
    
}
