package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.HallPlanRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallPlanService;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleHallPlanService implements HallPlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHallPlanService.class);

    private HallPlanRestClient hallPlanRestClient;
    private TicketRestClient ticketRestClient;

    private TicketDTO savedTicket;

    public SimpleHallPlanService(HallPlanRestClient hallPlanRestClient, TicketRestClient ticketRestClient){
        this.hallPlanRestClient = hallPlanRestClient;
        this.ticketRestClient = ticketRestClient;
    }

    @Override
    public HallDTO findHallByPerformanceId(Long id) throws DataAccessException {
        LOGGER.info("Find a hall by performance id " + id);
        return hallPlanRestClient.findHallByPerformanceId(id);
    }

    @Override
    public List<SimpleTicketSeatDTO> findTicketSeatByPerformanceId(Long performance_id) throws DataAccessException{
        LOGGER.info("Find a ticket seat by performance id "+ performance_id);
        return hallPlanRestClient.findTicketSeatsByPerformanceId(performance_id);
    }

    @Override
    public void save(PerformanceDTO performanceDTO, List<SimpleTicketSeatDTO> seatDTOS) throws DataAccessException, SeatSelectionException {
        LOGGER.info("Saving ticket seat dto to performance");
        List<Long> seatIds = new ArrayList<>();
        seatDTOS.forEach(s -> seatIds.add(s.getId()));
        savedTicket = ticketRestClient.save(performanceDTO, seatIds);
    }

    @Override
    public void save(TicketDTO ticketDTO, List<SimpleTicketSeatDTO> seatDTOS) throws SeatSelectionException, DataAccessException {
        LOGGER.info("Saving a ticket seat dto to ticket");
        List<Long> seatIds = new ArrayList<>();
        seatDTOS.forEach(s -> seatIds.add(s.getId()));
        savedTicket = ticketRestClient.save(ticketDTO, seatIds);
    }

    @Override
    public String requestNewReservationNumber() throws DataAccessException{
        LOGGER.info("Request a new reservation number");
        return ticketRestClient.requestNewReservationNumber();
    }

    @Override
    public Double calculateTicketSubTotal(PerformanceDTO performanceDTO, List<SeatDTO> seatDTOS) {
        LOGGER.info("Calculating ticket sub total price");
        Double subTotal = 0d;
        Double basePrice = performanceDTO.getBasePrice();

        for(SeatDTO seatDTO : seatDTOS){
            subTotal += basePrice*seatDTO.getMultiplier();
        }

        return subTotal;
    }

    @Override
    public File getBackgroundImage(Long hall_id, String language) throws DataAccessException {
        LOGGER.info("Loading background image");
        String filename = String.format("hallplan%d_%s.jpg",hall_id,language);
        File file = new File(System.getProperty("java.io.tmpdir") + filename);
        if(!file.exists()) {
            byte[] data = hallPlanRestClient.getBackgroundImage(hall_id, language);
            try {
                Files.write(file.toPath(),data);
            } catch (IOException e) {
                throw new DataAccessException("temp cache error");
            }
        }
        return file;
    }

    public TicketDTO getSavedTicket(){
        return savedTicket;
    }

    public void setSavedTicket(TicketDTO ticket){
        this.savedTicket = ticket;
    }
}
