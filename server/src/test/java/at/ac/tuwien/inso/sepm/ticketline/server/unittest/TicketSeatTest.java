package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketSeatStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketSeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketSeatService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleTicketSeatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketSeatTest {

    private static final Long TICKET_SEAT_ID = 1L;
    private static final TicketSeatStatus TICKET_SEAT_STATUS = TicketSeatStatus.SOLD;
    private static final Performance PERFORMANCE = Performance.builder()
        .id(1L)
        .artists(null)
        .event(null)
        .hall(null)
        .startDateTime(LocalDateTime.now())
        .endDateTime(LocalDateTime.now())
        .basePrice(40d)
        .leftCapacity(55)
        .build();
    private static final Seat SEAT = Seat.builder()
        .id(1L)
        .sector(1)
        .row(1)
        .number(1)
        .build();
    private static final Customer CUSTOMER = Customer.builder()
        .id(1L)
        .firstname("Max")
        .surname("Mustermann")
        .birthday(LocalDate.now())
        .email("max.mustermann@mustermail.com")
        .address("Argentinierstrasse 8")
        .phoneNumber("066688888888")
        .build();
    private static final Ticket TICKET = Ticket.builder()
        .performance(PERFORMANCE)
        .reservationNumber("01234567")
        .price(250d)
        .customer(CUSTOMER)
        .id(1L)
        .dateOfIssue(LocalDateTime.now())
        .status(TicketStatus.S)
        .build();

    @MockBean
    private TicketSeatRepository ticketSeatRepository;
    private TicketSeatService ticketSeatService;

    private TicketSeat ticketSeat;
    private Map<Long, TicketSeat> ticketSeatMap;
    private List<TicketSeat> ticketSeatList;
    private List<SimpleTicketSeatDTO> simpleTicketSeatDTOList;

    @Before
    public void setUp(){
        ticketSeatService = new SimpleTicketSeatService(ticketSeatRepository);
        ticketSeat = TicketSeat.builder()
            .id(TICKET_SEAT_ID)
            .seat(SEAT)
            .performance(PERFORMANCE)
            .ticket(TICKET)
            .status(TICKET_SEAT_STATUS)
            .build();

        ticketSeatList = new ArrayList<>();
        ticketSeatList.add(ticketSeat);

        SimpleTicketSeatDTO simpleTicketSeatDTO = new SimpleTicketSeatDTOImpl(SEAT.getId(), SEAT.getSector(), SEAT.getRow(),
            SEAT.getNumber(),SeatType.STAND.name(), 1d, 111, 222,
            0, TICKET_SEAT_STATUS.name(), TICKET.getId());
        simpleTicketSeatDTOList = new ArrayList<>();
        simpleTicketSeatDTOList.add(simpleTicketSeatDTO);

        ticketSeatMap = new HashMap<>();
        ticketSeatMap.put(TICKET_SEAT_ID, ticketSeat);

        BDDMockito.doAnswer(inv -> {
            TicketSeat ticketSeat1 = inv.getArgument(0);
            if (ticketSeat1.getId() == null){
                ticketSeat1.setId(1L + ticketSeatMap.size());
            }
            ticketSeatMap.put(ticketSeat1.getId(),ticketSeat1);
            return ticketSeat1;
        }).when(ticketSeatRepository).save(any(TicketSeat.class));
    }

    @Test
    public void findTicketSeatsByTicketId(){
        Long ticketId = 1L;
        BDDMockito.
            given(ticketSeatRepository.findTicketSeatsByTicket_Id(ticketId)).
            willReturn(ticketSeatList);


        List<TicketSeat> actualTicketSeat = ticketSeatService.findByTicketId(ticketId);

        assertThat(actualTicketSeat).isEqualTo(ticketSeatList);
    }

    @Test
    public void countTicketSeatByTicketId(){
        Integer count = 1;
        BDDMockito.
            given(ticketSeatRepository.countTicketSeatByTicketId(1L)).
            willReturn(count);

        Integer actualCount = ticketSeatService.countTicketSeatByTicketId(1L);

        assertEquals(count, actualCount);
    }

    @Test
    public void findSimpleTicketSeatsByPerformanceId(){
        Long perfprmanceId = 1L;
        BDDMockito.
            given(ticketSeatRepository.findTicketSeatsByPerformanceId(perfprmanceId)).
            willReturn(simpleTicketSeatDTOList);

        List<SimpleTicketSeatDTOImpl> actual = ticketSeatService.findByPerformanceId(perfprmanceId);

        assertEquals(simpleTicketSeatDTOList.get(0).getTicketId(), actual.get(0).getTicketId());
    }
}
