package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ticket_id")
    @SequenceGenerator(name = "seq_ticket_id", sequenceName = "seq_ticket_id")
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    @Column(nullable = false)
    private LocalDateTime dateOfIssue;


    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + id +
            ", price=" + price +
            ", reservationNumber='" + reservationNumber + '\'' +
            ", performance=" + performance +
            ", customer=" + customer +
            ", status=" + status +
            ", dateOfIssue=" + dateOfIssue +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) &&
            Objects.equals(price, ticket.price) &&
            Objects.equals(reservationNumber, ticket.reservationNumber) &&
            Objects.equals(performance, ticket.performance) &&
            Objects.equals(customer, ticket.customer) &&
            status == ticket.status &&
            Objects.equals(dateOfIssue, ticket.dateOfIssue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, price, reservationNumber, performance, customer, status, dateOfIssue);
    }

    public LocalDateTime getDateOfIssue() {

        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDateTime dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static TicketBuilder builder(){
        return new TicketBuilder();
    }

    public static final class TicketBuilder{

        private Long id;
        private Double price;
        private String reservationNumber;
        private Performance performance;
        private Customer customer;
        private TicketStatus status;
        private LocalDateTime dateOfIssue;

        public TicketBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TicketBuilder price(Double price){
            this.price = price;
            return this;
        }

        public TicketBuilder reservationNumber(String reservationNumber){
            this.reservationNumber = reservationNumber;
            return this;
        }

        public TicketBuilder performance(Performance performance){
            this.performance = performance;
            return this;
        }

        public TicketBuilder customer(Customer customer){
            this.customer = customer;
            return this;
        }

        public TicketBuilder status(TicketStatus status){
            this.status = status;
            return this;
        }

        public TicketBuilder dateOfIssue(LocalDateTime dateOfIssue){
            this.dateOfIssue = dateOfIssue;
            return this;
        }

        public Ticket build(){
            Ticket ticket = new Ticket();
            ticket.setId(id);
            ticket.setPrice(price);
            ticket.setReservationNumber(reservationNumber);
            ticket.setPerformance(performance);
            ticket.setCustomer(customer);
            ticket.setStatus(status);
            ticket.setDateOfIssue(dateOfIssue);
            return ticket;
        }
    }
}