package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.HallType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat.SeatMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class HallMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class HallMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private HallMapper hallMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private LocationMapper locationMapper;

    private static final Long HALL_ID = 1L;
    private static final String HALL_DESCRIPTION = "Description";
    private static final HallType HALL_TYPE = HallType.SEAT;
    private static final Integer HALL_CAPACITY = 100;
    private static final List<Seat> SEATS = new ArrayList<>();
    private static final Location LOCATION = Location.builder()
        .id(1L)
        .houseNumber(11)
        .city("City")
        .country("Country")
        .street("Street")
        .zip("Zip")
        .description("Description")
        .build();

    @Before
    public void setUp(){
        Seat seat = Seat.builder()
            .id(1L)
            .sector(1)
            .row(1)
            .number(1)
            .build();
        SEATS.add(seat);
    }

    @Test
    public void shouldMapHallDTOToHall(){
        LocationDTO locationDTO = locationMapper.locationToLocationDTO(LOCATION);
        List<SeatDTO> seatDTOS = seatMapper.seatToSeatDTO(SEATS);
        HallDTO hallDTO = HallDTO.builder()
            .id(HALL_ID)
            .type(HALL_TYPE.name())
            .description(HALL_DESCRIPTION)
            .capacity(HALL_CAPACITY)
            .seats(seatDTOS)
            .location(locationDTO)
            .builder();

        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        assertThat(hall).isNotNull();
        assertThat(hall.getId()).isEqualTo(HALL_ID);
        assertThat(hall.getCapacity()).isEqualTo(HALL_CAPACITY);
        assertThat(hall.getDescription()).isEqualTo(HALL_DESCRIPTION);
        assertThat(hall.getType()).isEqualTo(HALL_TYPE);
        assertThat(hall.getLocation()).isEqualTo(LOCATION);
        assertThat(hall.getSeats()).isEqualTo(SEATS);
    }

    @Test
    public void shouldMapHallToHallDTO(){
        Hall hall = Hall.builder()
            .id(HALL_ID)
            .capacity(HALL_CAPACITY)
            .description(HALL_DESCRIPTION)
            .type(HallType.SEAT)
            .location(LOCATION)
            .seats(SEATS)
            .build();

        HallDTO hallDTO = hallMapper.hallToHallDTO(hall);

        LocationDTO locationDTO = locationMapper.locationToLocationDTO(LOCATION);
        List<SeatDTO> seatDTOS = seatMapper.seatToSeatDTO(SEATS);
        assertThat(hallDTO).isNotNull();
        assertThat(hallDTO.getId()).isEqualTo(HALL_ID);
        assertThat(hallDTO.getCapacity()).isEqualTo(HALL_CAPACITY);
        assertThat(hallDTO.getDescription()).isEqualTo(HALL_DESCRIPTION);
        assertThat(hallDTO.getType()).isEqualTo(HALL_TYPE.name());
        assertThat(hallDTO.getLocation()).isEqualTo(locationDTO);
        assertThat(hallDTO.getSeats()).isEqualTo(seatDTOS);
    }
}
