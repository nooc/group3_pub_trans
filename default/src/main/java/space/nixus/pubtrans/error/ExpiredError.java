package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Any expired request operation error (can span requests).
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
public class ExpiredError extends RuntimeException { 
}
