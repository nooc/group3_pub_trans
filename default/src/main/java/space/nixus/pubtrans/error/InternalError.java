package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Any internal error.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalError extends RuntimeException {
}
