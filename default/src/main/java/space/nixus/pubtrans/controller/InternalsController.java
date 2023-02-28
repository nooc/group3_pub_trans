package space.nixus.pubtrans.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalsController {

    @GetMapping("/internals/cleaner")
    void cleanExpired() {
        //TODO cleanup expired
    }
}
