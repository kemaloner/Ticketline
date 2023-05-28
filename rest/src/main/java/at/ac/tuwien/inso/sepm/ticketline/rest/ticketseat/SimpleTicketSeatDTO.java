package at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat;

public interface SimpleTicketSeatDTO {
    Long getId();
    Integer getSector();
    Integer getRow();
    Integer getNumber();
    String getType();
    Double getMultiplier();
    Integer getX();
    Integer getY();
    Integer getAngle();
    String getStatus();
    Long getTicketId();
}
