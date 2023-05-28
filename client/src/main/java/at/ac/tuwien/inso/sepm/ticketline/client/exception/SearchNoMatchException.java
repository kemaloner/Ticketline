package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class SearchNoMatchException extends Exception {

    public SearchNoMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchNoMatchException(String message) {
        super(message);
    }

}
