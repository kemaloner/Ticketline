package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.TicketSeat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketSeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketSeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleTicketSeatService implements TicketSeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketSeatService.class);

    private TicketSeatRepository ticketSeatRepository;

    public SimpleTicketSeatService(TicketSeatRepository ticketSeatRepository){
        this.ticketSeatRepository = ticketSeatRepository;
    }

    @Override
    public List<SimpleTicketSeatDTOImpl> findByPerformanceId(Long performance_id) {
        LOGGER.info("Finding a ticket with performance id " + performance_id);
        List<SimpleTicketSeatDTO> list = ticketSeatRepository.findTicketSeatsByPerformanceId(performance_id);
        List<SimpleTicketSeatDTOImpl> listImpl = new ArrayList<>();
        list.forEach(s -> listImpl.add(new SimpleTicketSeatDTOImpl(s)));
        return listImpl;
    }

    public List<TicketSeat> findByTicketId(Long ticketId) {
        LOGGER.info("Finding a ticket with id " + ticketId);
        if (ticketId == null) {
            throw new ServerServiceValidationException("Ticket is not valid!");
        }
        return ticketSeatRepository.findTicketSeatsByTicket_Id(ticketId);
    }

    @Override
    public Integer countTicketSeatByTicketId(Long ticketId) {
        LOGGER.info("Count of ticket seat by ticket id " + ticketId);
        return ticketSeatRepository.countTicketSeatByTicketId(ticketId);
    }
}
