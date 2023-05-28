package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hall")
@Api(value = "hall")
public class HallEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(HallEndpoint.class);


    private HallService hallService;
    private HallMapper hallMapper;

    public HallEndpoint(HallService hallService, HallMapper hallMapper){
        this.hallService = hallService;
        this.hallMapper = hallMapper;
    }

    @RequestMapping(value = "/performance_id/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "find the hall in which the performance with the given id plays")
    public HallDTO findByPerformanceId(@PathVariable("id") Long id){
        LOGGER.info("Find hall by performance id " + id);
        return hallMapper.hallToHallDTO(hallService.findByPerformanceId(id));
    }
}
