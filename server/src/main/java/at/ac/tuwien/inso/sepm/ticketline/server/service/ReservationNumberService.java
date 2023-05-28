package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationNumber;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationNumberService {

    /**
     * Registers a new unique reservation number into the database
     * @param reservationNumber ReservationNumber object, which will be saved
     *                          to the database. Usually a new one.
     * @return registered reservation number for use in a ticket
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    String save(ReservationNumber reservationNumber);
}
