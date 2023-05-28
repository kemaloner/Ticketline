package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SeatSelectionException extends Exception{

    private HttpStatus httpStatus;

    public SeatSelectionException(String message, HttpStatus httpStatus){
        this(message);
        this.httpStatus = httpStatus;
    }

    public SeatSelectionException(String message){
        super(message);
    }

    public SeatSelectionException(){}

}
