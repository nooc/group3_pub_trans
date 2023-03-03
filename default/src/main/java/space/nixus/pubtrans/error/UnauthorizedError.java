package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Any unauthorised error.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedError extends RuntimeException {   
}
