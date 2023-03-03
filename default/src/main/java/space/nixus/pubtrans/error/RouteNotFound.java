package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Route nor found by query.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RouteNotFound extends RuntimeException {
    public RouteNotFound() {}   
}
