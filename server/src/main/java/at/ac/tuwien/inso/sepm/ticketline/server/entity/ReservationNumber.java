package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;

@Entity
public class ReservationNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_reservation_number")
    @SequenceGenerator(name = "seq_reservation_number", sequenceName = "seq_reservation_number")
    private Long reservation_number;

    public Long getReservation_number() {
        return reservation_number;
    }

    public void setReservation_number(Long reservation_number) {
        this.reservation_number = reservation_number;
    }

    public static ReservationNumberBuilder builder(){
        return new ReservationNumberBuilder();
    }

    public static final class ReservationNumberBuilder{

        private Long reservation_number;

        public ReservationNumberBuilder reservation_number(Long reservation_number){
            this.reservation_number = reservation_number;
            return this;
        }

        public ReservationNumber build(){
            ReservationNumber reservationNumber = new ReservationNumber();
            reservationNumber.setReservation_number(reservation_number);
            return reservationNumber;
        }
    }
}
