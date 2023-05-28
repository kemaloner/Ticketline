package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticketseat.TicketSeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketSeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ticketSeat")
@Api(value = "ticketSeat")
public class TicketSeatEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketSeatEndpoint.class);


    private final TicketSeatService ticketSeatService;
    private final TicketSeatMapper ticketSeatMapper;

    public TicketSeatEndpoint(TicketSeatService ticketSeatService, TicketSeatMapper ticketSeatMapper){
        this.ticketSeatService = ticketSeatService;
        this.ticketSeatMapper = ticketSeatMapper;
    }

    @RequestMapping(value = "/findByTicketId/{ticketId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get a list of news which are not read by the user yet")
    public List<TicketSeatDTO> findByTicketId(@PathVariable("ticketId") Long ticketId){
        LOGGER.info("Loading ticket by ticket id " + ticketId);
        return ticketSeatMapper.ticketSeatToTicketSeatDTO(ticketSeatService.findByTicketId(ticketId));
    }
}
