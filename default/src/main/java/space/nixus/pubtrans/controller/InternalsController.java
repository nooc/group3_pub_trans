package space.nixus.pubtrans.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal operations like cron and task queues etc.
 */
@RestController
public final class InternalsController {

    @GetMapping("/internals/cleaner")
    void cleanExpired() {
        // db cleanup tasks
    }
}
