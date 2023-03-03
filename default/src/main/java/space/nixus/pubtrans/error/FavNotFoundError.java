package space.nixus.pubtrans.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Database Favorite not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FavNotFoundError extends RuntimeException {
    public FavNotFoundError() {}   
}
