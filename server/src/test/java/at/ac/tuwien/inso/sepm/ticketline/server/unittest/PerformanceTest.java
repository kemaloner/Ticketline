package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimplePerformanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PerformanceTest {

    private static final Long PERFORMANCE1_ID = 1L;
    private static final Double PERFORMANCE1_PRICE = 100D;
    private static final LocalDateTime PERFORMANCE1_ENDTIME = LocalDateTime.of(2018,8,
        4,22,10);
    private static final LocalDateTime PERFORMANCE1_STARTTIME = LocalDateTime.of(2018,8,
        4,20,00);
    private static final Integer PERFORMANCE1_LEFTCAPACITY =  100;
    private static final Long PERFORMANCE1_EVENTID = 1L;
    private static final Long PERFORMANCE1_HALLID = 1L;

    private static final Long PERFORMANCE2_ID = 2L;
    private static final Double PERFORMANCE2_PRICE = 120D;
    private static final LocalDateTime PERFORMANCE2_ENDTIME = LocalDateTime.of(2018,8,
        3,15,30);
    private static final LocalDateTime PERFORMANCE2_STARTTIME = LocalDateTime.of(2018,8,
        3,12,00);
    private static final Integer PERFORMANCE2_LEFTCAPACITY =  130;
    private static final Long PERFORMANCE2_EVENTID = 2L;
    private static final Long PERFORMANCE2_HALLID = 2L;

    private Performance performance1;
    private Performance performance2;
    private Optional<Performance> optionalPerformance1;
    private Optional<Performance> optionalPerformance2;

    private List<Performance> performanceList;

    @MockBean
    private PerformanceRepository performanceRepository;

    private PerformanceService performanceService;

    @Before
    public void setUp(){
        performanceService = new SimplePerformanceService(performanceRepository);

        Event event1 = new Event();
        event1.setId(PERFORMANCE1_EVENTID);
        Event event2 = new Event();
        event2.setId(PERFORMANCE2_EVENTID);

        Hall hall1 = new Hall();
        hall1.setId(PERFORMANCE1_HALLID);
        Hall hall2 = new Hall();
        hall2.setId(PERFORMANCE2_HALLID);

        performance1 = Performance.builder()
            .id(PERFORMANCE1_ID)
            .basePrice(PERFORMANCE1_PRICE)
            .endDateTime(PERFORMANCE1_ENDTIME)
            .startDateTime(PERFORMANCE1_STARTTIME)
            .leftCapacity(PERFORMANCE1_LEFTCAPACITY)
            .event(event1)
            .hall(hall1)
            .build();

        performance2 = Performance.builder()
            .id(PERFORMANCE2_ID)
            .basePrice(PERFORMANCE2_PRICE)
            .endDateTime(PERFORMANCE2_ENDTIME)
            .startDateTime(PERFORMANCE2_STARTTIME)
            .leftCapacity(PERFORMANCE2_LEFTCAPACITY)
            .event(event2)
            .hall(hall2)
            .build();

        performanceList = new ArrayList<>();
        optionalPerformance1 = Optional.of(performance1);
        optionalPerformance2 = Optional.of(performance2);
        performanceList.add(performance1);
        performanceList.add(performance2);
    }

    @Test
    public void shouldFindAllPerformances(){
        LocalDateTime localDateTime = LocalDateTime.now();
        PageRequest page = PageRequest.of(0,5);

        BDDMockito
            .given(performanceRepository.findByFilter(false, null, false,
                null,false, null, false, null, null,
                false, null, localDateTime, page))
            .willReturn(new PerformancePageStub(Arrays.asList(performance1, performance2)));

        Page<Performance> performancePage = performanceService.findByFilter(false, null, false,
            null,false, null, false, null, null,
            false, null, localDateTime, page);

        assertEquals(performancePage.getContent(), new PerformancePageStub(Arrays.asList(performance1, performance2)).getContent());
    }

    @Test
    public void shouldFindOnePerformance(){
        BDDMockito.given(performanceRepository.findById(PERFORMANCE1_ID)).willReturn(optionalPerformance1);


        Performance actualPerformance = performanceService.findById(PERFORMANCE1_ID);

        assertEquals(actualPerformance, optionalPerformance1.get());

    }

    @Test
    public void shouldFindCorrectPerformanceByCriteria(){
        LocalDateTime localDateTime = LocalDateTime.now();
        PageRequest page = PageRequest.of(0,5);

        BDDMockito
            .given(performanceRepository.findByFilter(false, null, false,
                null,true, 100d, false, null, null,
                false, null, localDateTime, page))
        .willReturn(new PerformancePageStub(Arrays.asList(performance1)));

        Page<Performance> performancePage = performanceService.findByFilter(false, null, false,
            null,true, 100d, false, null, null,
            false, null, localDateTime, page);

        assertEquals(performancePage.getContent(), new PerformancePageStub(Arrays.asList(performance1)).getContent());
    }

    private class PerformancePageStub extends PageImpl {

        private List<Performance> performanceList;

        public PerformancePageStub(List<Performance> performanceList){
            super(performanceList);
            this.performanceList = performanceList;
        }

        @Override
        public List<Performance> getContent(){
            return this.performanceList;
        }
    }

}
