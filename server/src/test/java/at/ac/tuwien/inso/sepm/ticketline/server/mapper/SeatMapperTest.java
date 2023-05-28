package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat.SeatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class SeatMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    private static final Long SEAT_ID = 1L;
    private static final Integer SEAT_SECTOR = 1;
    private static final Integer SECTOR_ROW = 1;
    private static final Integer SEAT_NUMBER = 1;
    private static final Double SEAT_MULTIPLIER = 1.2;
    private static final Integer SEAT_XCOORDINATE = 100;
    private static final Integer SEAT_YCOORDINATE = 100;
    private static final Integer SEAT_ANGLE = 0;

    @Test
    public void shouldMapSeatDTOToSeat(){
        SeatDTO seatDTO = SeatDTO.builder()
            .id(SEAT_ID)
            .sector(SEAT_SECTOR)
            .row(SECTOR_ROW)
            .number(SEAT_NUMBER)
            .multiplier(SEAT_MULTIPLIER)
            .xCoordinate(SEAT_XCOORDINATE)
            .yCoordinate(SEAT_YCOORDINATE)
            .angle(SEAT_ANGLE)
            .build();

        Seat seat = seatMapper.seatDTOToSeat(seatDTO);
        assertThat(seat).isNotNull();
        assertThat(seat.getId()).isEqualTo(SEAT_ID);
        assertThat(seat.getSector()).isEqualTo(SEAT_SECTOR);
        assertThat(seat.getRow()).isEqualTo(SECTOR_ROW);
        assertThat(seat.getNumber()).isEqualTo(SEAT_NUMBER);
        assertThat(seat.getMultiplier()).isEqualTo(SEAT_MULTIPLIER);
    }


    @Test
    public void shouldMapSeatToSeatDTO(){
        Seat seat = Seat.builder()
            .id(SEAT_ID)
            .sector(SEAT_SECTOR)
            .row(SECTOR_ROW)
            .number(SEAT_NUMBER)
            .multiplier(SEAT_MULTIPLIER)
            .xCoordinate(SEAT_XCOORDINATE)
            .yCoordinate(SEAT_YCOORDINATE)
            .angle(SEAT_ANGLE)
            .build();

        SeatDTO seatDTO = seatMapper.seatToSeatDTO(seat);
        assertThat(seatDTO).isNotNull();
        assertThat(seatDTO.getId()).isEqualTo(SEAT_ID);
        assertThat(seatDTO.getSector()).isEqualTo(SEAT_SECTOR);
        assertThat(seatDTO.getRow()).isEqualTo(SECTOR_ROW);
        assertThat(seatDTO.getNumber()).isEqualTo(SEAT_NUMBER);
        assertThat(seatDTO.getMultiplier()).isEqualTo(SEAT_MULTIPLIER);
    }


}
