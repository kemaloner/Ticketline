package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(value = "TicketDTO", description = "A simple DTO for ticket entries via rest")
public class TicketDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The price of the ticket")
    private Double price;

    @ApiModelProperty(required = true, readOnly = true, name = "The reservation number of the ticket")
    private String reservationNumber;

    @ApiModelProperty(required = true, readOnly = true, name = "The performance of the ticket")
    private PerformanceDTO performance;

    @ApiModelProperty(required = true, readOnly = true, name = "The customer of the ticket")
    private CustomerDTO customer;

    @ApiModelProperty(required = true, readOnly = true, name = "The status of the ticket seat")
    private TicketStatus status;

    @ApiModelProperty(required = true, readOnly = true, name = "The date in which this ticket is issued")
    private LocalDateTime dateOfIssue;

    @Override
    public String toString() {
        return "TicketDTO{" +
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
        TicketDTO ticketDTO = (TicketDTO) o;
        return Objects.equals(id, ticketDTO.id) &&
            Objects.equals(price, ticketDTO.price) &&
            Objects.equals(reservationNumber, ticketDTO.reservationNumber) &&
            Objects.equals(performance, ticketDTO.performance) &&
            Objects.equals(customer, ticketDTO.customer) &&
            status == ticketDTO.status &&
            Objects.equals(dateOfIssue, ticketDTO.dateOfIssue);
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

    public PerformanceDTO getPerformance() {
        return performance;
    }

    public void setPerformance(PerformanceDTO performance) {
        this.performance = performance;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }


    public static TicketDTOBuilder builder(){
        return new TicketDTOBuilder();
    }

    public static final class TicketDTOBuilder{

        private Long id;
        private Double price;
        private String reservationNumber;
        private PerformanceDTO performance;
        private CustomerDTO customer;
        private TicketStatus status;
        private LocalDateTime dateOfIssue;

        public TicketDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TicketDTOBuilder price(Double price){
            this.price = price;
            return this;
        }

        public TicketDTOBuilder reservationNumber(String reservationNumber){
            this.reservationNumber = reservationNumber;
            return this;
        }

        public TicketDTOBuilder performance(PerformanceDTO performance){
            this.performance = performance;
            return this;
        }

        public TicketDTOBuilder customer(CustomerDTO customer){
            this.customer = customer;
            return this;
        }

        public TicketDTOBuilder status(TicketStatus status){
            this.status = status;
            return this;
        }

        public TicketDTOBuilder dateOfIssue(LocalDateTime dateOfIssue){
            this.dateOfIssue = dateOfIssue;
            return this;
        }

        public TicketDTO build(){
            TicketDTO ticket = new TicketDTO();
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
