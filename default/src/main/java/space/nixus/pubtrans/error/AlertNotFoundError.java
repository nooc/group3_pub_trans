package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Database Alert not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AlertNotFoundError extends RuntimeException {
    public AlertNotFoundError() {}   
}
