package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class ClientException extends RuntimeException {
    public ClientException() {
        super();
    }
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
    public ClientException(String message) {
        super(message);
    }
    public ClientException(Throwable cause) {
        super(cause);
    }
}
