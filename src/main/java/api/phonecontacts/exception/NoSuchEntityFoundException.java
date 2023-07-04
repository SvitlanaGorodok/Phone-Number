package api.phonecontacts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchEntityFoundException extends RuntimeException{
    public NoSuchEntityFoundException(String message) {
        super(message);
    }
}
