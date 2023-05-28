package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationNumber;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationNumberRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationNumberService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleReservationNumberService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationNumberTest {

    private static final Long RESERVATION_NUMBER = 1l;

    @MockBean
    private ReservationNumberRepository reservationNumberRepository;
    private ReservationNumberService reservationNumberService;

    private ReservationNumber reservationNumber;
    private Map<Long, ReservationNumber> reservationNumberMap;

    @Before
    public void setUp(){
        reservationNumberService = new SimpleReservationNumberService(reservationNumberRepository);
        reservationNumber = ReservationNumber.builder()
            .reservation_number(RESERVATION_NUMBER)
            .build();

        reservationNumberMap = new HashMap<>();
        reservationNumberMap.put(RESERVATION_NUMBER, reservationNumber);

        BDDMockito.doAnswer(inv -> {
            ReservationNumber reservationNumber1 = inv.getArgument(0);
            if (reservationNumber1.getReservation_number() == null){
                reservationNumber1.setReservation_number(1L + reservationNumberMap.size());
            }
            reservationNumberMap.put(reservationNumber1.getReservation_number(),reservationNumber1);
            return reservationNumber1;
        }).when(reservationNumberRepository).save(any(ReservationNumber.class));

    }

    @Test
    public void saveReservationNumber(){
        ReservationNumber newReservationNumber = ReservationNumber.builder()
            .reservation_number(2L)
            .build();
        LocalDate today = LocalDate.now();
        String expected = "TL"+(today.getYear()-2000)+""+today.getMonthValue()+""+today.getDayOfMonth()+""+newReservationNumber.getReservation_number();

        String actual = reservationNumberService.save(newReservationNumber);

        assertEquals(expected, actual);
    }
}
