package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationNumberRepository extends JpaRepository<ReservationNumber, Long> {

}
