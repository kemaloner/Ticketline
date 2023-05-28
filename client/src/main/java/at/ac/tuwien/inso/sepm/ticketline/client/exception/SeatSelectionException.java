package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class SeatSelectionException extends Exception{
    public SeatSelectionException() {
    }

    public SeatSelectionException(String message) {
        super(message);
    }

    public SeatSelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
