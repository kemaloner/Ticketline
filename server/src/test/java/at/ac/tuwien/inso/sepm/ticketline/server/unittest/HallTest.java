package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.HallType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HallService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHallService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class HallTest {

    private static final Long HALL_ID = 1L;
    private static final String HALL_DESCRIPTION = "Description";
    private static final HallType HALL_TYPE = HallType.STAND;
    private static final Integer HALL_CAPACITY = 100;
    private static final Location LOCATION = Location.builder()
        .id(1L)
        .houseNumber(11)
        .city("City")
        .country("Country")
        .street("Street")
        .zip("Zip")
        .description("Description")
        .build();
    private static final List<Seat> SEATS = new ArrayList<>();

    @MockBean
    private HallRepository hallRepository;
    private HallService hallService;

    private Hall hall;
    private Map<Long, Hall> hallMap;

    @Before
    public void setUp(){
        hallService = new SimpleHallService(hallRepository);
        Seat seat = Seat.builder()
            .id(1L)
            .sector(1)
            .row(1)
            .number(1)
            .build();
        SEATS.add(seat);

        hall = Hall.builder()
            .id(HALL_ID)
            .location(LOCATION)
            .type(HALL_TYPE)
            .description(HALL_DESCRIPTION)
            .capacity(HALL_CAPACITY)
            .seats(SEATS)
            .build();

        hallMap = new HashMap<>();
        hallMap.put(HALL_ID, hall);

        BDDMockito.doAnswer(inv -> {
            Hall hall1 = inv.getArgument(0);
            if (hall1.getId() == null){
                hall1.setId(1L + hallMap.size());
            }
            hallMap.put(hall1.getId(),hall1);
            return hall1;
        }).when(hallRepository).save(any(Hall.class));
    }

    @Test
    public void findOneHallByPerformanceId(){
        Long performanceId = 1L;
        BDDMockito.
            given(hallRepository.findByPerformanceId(performanceId)).
            willReturn(hall);

        Hall actualHall = hallService.findByPerformanceId(performanceId);

        assertEquals(hall, actualHall);
    }
}
