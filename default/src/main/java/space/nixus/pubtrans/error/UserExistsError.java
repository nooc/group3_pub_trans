package space.nixus.pubtrans.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * User exists when trying to create one.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserExistsError extends RuntimeException {
    public UserExistsError() {}
    public UserExistsError(String username) {
        super("Username: " + username);
    }
}
