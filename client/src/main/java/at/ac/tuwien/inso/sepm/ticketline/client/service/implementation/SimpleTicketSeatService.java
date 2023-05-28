package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketSeatRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketSeatService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleTicketSeatService implements TicketSeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketSeatService.class);
    private final TicketSeatRestClient ticketSeatRestClient;

    public SimpleTicketSeatService(TicketSeatRestClient ticketSeatRestClient){
        this.ticketSeatRestClient = ticketSeatRestClient;
    }

    @Override
    public List<TicketSeatDTO> findByTicketId(Long ticketId) throws DataAccessException , ClientServiceValidationException{
        LOGGER.info("Finding a ticket with id " + ticketId);
        if (ticketId == null){
            throw new ClientServiceValidationException("Ticket is not valid!");
        }
        return ticketSeatRestClient.findByTicketId(ticketId);
    }
}
