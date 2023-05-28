package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketSeatStatus;

import javax.persistence.*;
import java.util.Objects;



@Entity
@SqlResultSetMapping(
    name = "simpleTicketSeatMapping",
    columns={
        @ColumnResult(name = "id", type=Long.class),
        @ColumnResult(name = "sector", type=Integer.class),
        @ColumnResult(name = "row", type=Integer.class),
        @ColumnResult(name = "number", type=Integer.class),
        @ColumnResult(name = "type", type=String.class),
        @ColumnResult(name = "multiplier", type=Double.class),
        @ColumnResult(name = "x", type=Integer.class),
        @ColumnResult(name = "y", type=Integer.class),
        @ColumnResult(name = "angle", type=Integer.class),
        @ColumnResult(name = "status", type=String.class),
        @ColumnResult(name = "ticketId", type=Long.class)
    }
)

@NamedNativeQuery(
    name="TicketSeat.findTicketSeatsByPerformanceId",
    resultSetMapping="simpleTicketSeatMapping",
    query="SELECT s.id,s.angle,s.multiplier,s.number,s.row,s.sector,s.type,"+
        "s.x_coordinate as x, s.y_coordinate as y,  " +
        "(SELECT ticket_id FROM ticket_seat " +
        "WHERE performance_id=:performance_id AND seat_id=s.id AND status != 'FREE'" +
        "ORDER BY ID DESC LIMIT 0,1) as ticketId, " +
        "(SELECT status FROM ticket_seat " +
        "WHERE performance_id=:performance_id AND seat_id=s.id AND status != 'FREE'" +
        "ORDER BY ID DESC LIMIT 0,1) status " +
        "FROM seat s " +
        "WHERE hall_id=(SELECT hall_id FROM performance WHERE id=:performance_id)")


@Table(name = "ticket_seat")
public class TicketSeat {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ticket_seat_id")
    @SequenceGenerator(name = "seq_ticket_seat_id", sequenceName = "seq_ticket_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TicketSeatStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public TicketSeatStatus getStatus() {
        return status;
    }

    public void setStatus(TicketSeatStatus status) {
        this.status = status;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketSeat that = (TicketSeat) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(ticket, that.ticket) &&
            Objects.equals(seat, that.seat) &&
            status == that.status &&
            Objects.equals(performance, that.performance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ticket, seat, status, performance);
    }

    @Override
    public String toString() {
        return "TicketSeat{" +
            "id=" + id +
            ", ticket=" + ticket +
            ", seat=" + seat +
            ", status=" + status +
            ", performance=" + performance +
            '}';
    }

    public static TicketSeatBuilder builder(){
        return new TicketSeatBuilder();
    }

    public static final class TicketSeatBuilder{

        private Long id;
        private Ticket ticket;
        private Seat seat;
        private TicketSeatStatus status;
        private Performance performance;

        public TicketSeatBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TicketSeatBuilder ticket(Ticket ticket){
            this.ticket = ticket;
            return this;
        }

        public TicketSeatBuilder seat(Seat seat){
            this.seat = seat;
            return this;
        }

        public TicketSeatBuilder status(TicketSeatStatus status){
            this.status = status;
            return this;
        }

        public TicketSeatBuilder performance(Performance performance){
            this.performance = performance;
            return this;
        }

        public TicketSeat build(){
            TicketSeat ticketSeat = new TicketSeat();
            ticketSeat.setId(id);
            ticketSeat.setTicket(ticket);
            ticketSeat.setStatus(status);
            ticketSeat.setSeat(seat);
            ticketSeat.setPerformance(performance);
            return ticketSeat;
        }
    }
}
