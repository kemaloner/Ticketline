package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationNumber;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationNumberRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SimpleReservationNumberService implements ReservationNumberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleReservationNumberService.class);

    private ReservationNumberRepository reservationNumberRepository;

    public SimpleReservationNumberService(ReservationNumberRepository reservationNumberRepository){
        this.reservationNumberRepository = reservationNumberRepository;
    }

    @Override
    public String save(ReservationNumber reservationNumber) {
        LOGGER.info("Supplying a new reservation number");
        ReservationNumber temp = reservationNumberRepository.save(reservationNumber);
        LocalDate today = LocalDate.now();
        return "TL"+(today.getYear()-2000)+""+today.getMonthValue()+""+today.getDayOfMonth()+""+reservationNumber.getReservation_number();
    }
}
