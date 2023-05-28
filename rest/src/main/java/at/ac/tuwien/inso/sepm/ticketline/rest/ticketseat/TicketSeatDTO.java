package at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "TicketSeatDTO", description = "A simple DTO for ticket seat entries via rest")
public class TicketSeatDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The ticket of the ticket seat")
    private TicketDTO ticket;

    @ApiModelProperty(required = true, readOnly = true, name = "The seat of the ticket seat")
    private SeatDTO seat;

    @ApiModelProperty(required = true, readOnly = true, name = "The status of the ticket seat")
    private String status;

    @ApiModelProperty(required = true, readOnly = true, name = "The performance to which this seat belongs")
    private PerformanceDTO performance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PerformanceDTO getPerformance() {
        return performance;
    }

    public void setPerformance(PerformanceDTO performance) {
        this.performance = performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketSeatDTO that = (TicketSeatDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(ticket, that.ticket) &&
            Objects.equals(seat, that.seat) &&
            Objects.equals(status, that.status) &&
            Objects.equals(performance, that.performance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ticket, seat, status, performance);
    }

    @Override
    public String toString() {
        return "TicketSeatDTO{" +
            "id=" + id +
            ", ticket=" + ticket +
            ", seat=" + seat +
            ", status='" + status + '\'' +
            ", performanceDTO=" + performance +
            '}';
    }

    public static TicketSeatDTOBuilder builder(){
        return new TicketSeatDTOBuilder();
    }

    public static final class TicketSeatDTOBuilder{

        private Long id;
        private TicketDTO ticket;
        private SeatDTO seat;
        private String status;
        private PerformanceDTO performance;

        public TicketSeatDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TicketSeatDTOBuilder ticket(TicketDTO ticket){
            this.ticket = ticket;
            return this;
        }

        public TicketSeatDTOBuilder seat(SeatDTO seat){
            this.seat = seat;
            return this;
        }

        public TicketSeatDTOBuilder status(String status){
            this.status = status;
            return this;
        }

        public TicketSeatDTOBuilder performance(PerformanceDTO performance){
            this.performance = performance;
            return this;
        }

        public TicketSeatDTO build(){
            TicketSeatDTO ticketSeat = new TicketSeatDTO();
            ticketSeat.setId(id);
            ticketSeat.setTicket(ticket);
            ticketSeat.setStatus(status);
            ticketSeat.setSeat(seat);
            ticketSeat.setPerformance(performance);
            return ticketSeat;
        }
    }
}
