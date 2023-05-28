package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/performance")
@Api(value = "performance")
public class PerformanceEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceEndpoint.class);


    private final PerformanceMapper performanceMapper;
    private final PerformanceService performanceService;

    PerformanceEndpoint(PerformanceMapper performanceMapper, PerformanceService performanceService){
        this.performanceMapper = performanceMapper;
        this.performanceService = performanceService;
    }

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific performance entry")
    public PerformanceDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Loading a performance by id "+ id);
        return performanceMapper.performanceToPerformanceDTO(performanceService.findById(id));
    }

    @RequestMapping(value = "/findByFilter", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all performances with given filter")
    public PaginationWrapper<PerformanceDTO> findByFilter(@RequestParam HashMap<String, String> hashMap,
                                                          Pageable pageable) {
        LOGGER.info("Loading performance by filter");

        boolean locationFilter = false;
        boolean eventFilter = false;
        boolean priceFilter = false;
        boolean dateTimeFilter = false;
        boolean artistFilter = false;
        Long locationId = null;
        Long eventId = null;
        Double price = null;
        LocalDateTime dateTimeFrom = null;
        LocalDateTime dateTimeTo = null;
        Long artistId = null;
        LocalDateTime currentDateTime = null;

        if(hashMap.containsKey("locationId")){
            locationFilter = true;
            locationId = Long.parseLong(hashMap.get("locationId"));
        }
        if(hashMap.containsKey("eventId")){
            eventFilter = true;
            eventId = Long.parseLong(hashMap.get("eventId"));
        }
        if(hashMap.containsKey("price")){
            priceFilter = true;
            price = Double.parseDouble(hashMap.get("price"));
        }
        if(hashMap.containsKey("dateTimeFrom")){
            dateTimeFilter = true;
            dateTimeFrom = LocalDateTime.parse(hashMap.get("dateTimeFrom"));
        }
        if(hashMap.containsKey("dateTimeTo")){
            dateTimeFilter = true;
            dateTimeTo = LocalDateTime.parse(hashMap.get("dateTimeTo"));
        }
        if (hashMap.containsKey("artistId")){
            artistFilter = true;
            artistId = Long.parseLong(hashMap.get("artistId"));
        }

        currentDateTime = LocalDateTime.now();

        Page<Performance> performanceList = performanceService.findByFilter(locationFilter,
            locationId, eventFilter, eventId, priceFilter, price, dateTimeFilter, dateTimeFrom,
            dateTimeTo, artistFilter, artistId, currentDateTime, pageable);
        List<PerformanceDTO> performanceDTOList = performanceMapper.performanceToPerformanceDTO(performanceList.getContent());
        int pageTotal = performanceList.getTotalPages();
        return new PaginationWrapper<>(performanceDTOList, pageTotal);
    }
}
