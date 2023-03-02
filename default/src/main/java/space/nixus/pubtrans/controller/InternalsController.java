package space.nixus.pubtrans.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class InternalsController {

    @GetMapping("/internals/cleaner")
    void cleanExpired() {
        // db cleanup tasks
    }
}
