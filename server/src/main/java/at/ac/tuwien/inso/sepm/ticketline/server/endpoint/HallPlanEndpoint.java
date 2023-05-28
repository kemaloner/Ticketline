package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTOImpl;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticketseat.TicketSeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HallService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketSeatService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/hall_plan")
@Api(value = "hall_plan")
public class HallPlanEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(HallPlanEndpoint.class);


    private HallService hallService;
    private HallMapper hallMapper;
    private TicketSeatService ticketSeatService;
    private TicketSeatMapper ticketSeatMapper;

    public HallPlanEndpoint(HallService hallService, HallMapper hallMapper, TicketSeatService ticketSeatService, TicketSeatMapper ticketSeatMapper){
        this.hallMapper = hallMapper;
        this.hallService = hallService;
        this.ticketSeatService = ticketSeatService;
        this.ticketSeatMapper = ticketSeatMapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @RequestMapping(value = "/performance_id/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "find all occupied and unoccupied seats, together with their corresponding tickets")
    public List<SimpleTicketSeatDTOImpl> findByPerformanceId(@PathVariable("id") Long id){
        LOGGER.info("Loading hallplan by performance id " +id);
        return ticketSeatService.findByPerformanceId(id);
    }

    @RequestMapping(value = "/image/{id}/{lang}", method = RequestMethod.GET)
    @ApiOperation(value = "Get background image of hall")
    public ResponseEntity<byte[]> getBackgroundImage(@PathVariable Long id, @PathVariable String lang) {
        LOGGER.info("Loading background image");
        String filename = String.format("hallplan%d_%s.jpg",id,lang);
        try {
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(FileUtil.readAllBytes(filename));
        } catch (IOException e) {
            throw new NotFoundException(e.getClass().getName() + e.getMessage());
        }
    }
}
