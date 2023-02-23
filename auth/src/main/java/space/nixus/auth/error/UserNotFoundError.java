package space.nixus.auth.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundError extends RuntimeException {
    public UserNotFoundError() {}
    public UserNotFoundError(long id) {
        super("User-id: " + id);
    }    
}
