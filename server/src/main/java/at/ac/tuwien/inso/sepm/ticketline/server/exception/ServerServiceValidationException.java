package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ServerServiceValidationException extends RuntimeException{

    public ServerServiceValidationException(String message){
        super("ServerServiceValidationException: " + message);
    }
}
