package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/location")
@Api(value = "location")
public class LocationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationEndpoint.class);


    private final LocationMapper locationMapper;
    private final LocationService locationService;

    LocationEndpoint(LocationMapper locationMapper, LocationService locationService){
        this.locationMapper = locationMapper;
        this.locationService = locationService;
    }

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific location entry")
    public LocationDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Find a location by id " + id);
        return locationMapper.locationToLocationDTO(locationService.findById(id));
    }

    @RequestMapping(value = "/findByFilter", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all locations with given filter")
    public PaginationWrapper<LocationDTO> findByFilter(@RequestParam HashMap<String, String> hashMap, Pageable request) {
        LOGGER.info("Loading locations by filter");

        boolean descriptionFilter = false;
        boolean streetFilter = false;
        boolean cityFilter = false;
        boolean countryFilter = false;
        boolean zipFilter = false;
        boolean artistFilter = false;
        boolean eventFilter = false;
        String description = "";
        String street = "";
        String city = "";
        String country = "";
        String zip = "";
        Long eventId = null;
        Long artistId = null;

        if(hashMap.containsKey("description")){
            descriptionFilter = true;
            description = hashMap.get("description");
        }
        if(hashMap.containsKey("street")){
            streetFilter = true;
            street = hashMap.get("street");
        }
        if(hashMap.containsKey("city")){
            cityFilter = true;
            city = hashMap.get("city");
        }
        if(hashMap.containsKey("country")){
            countryFilter = true;
            country = hashMap.get("country");
        }
        if(hashMap.containsKey("zip")){
            zipFilter = true;
            zip = hashMap.get("zip");
        }
        if (hashMap.containsKey("artistId")){
            artistFilter = true;
            artistId = Long.parseLong(hashMap.get("artistId"));
        }
        if(hashMap.containsKey("eventId")){
            eventFilter = true;
            eventId = Long.parseLong(hashMap.get("eventId"));
        }

        Page<Location> result = locationService.findByFilter(descriptionFilter, description, streetFilter,
            street, cityFilter, city, countryFilter, country, zipFilter, zip, artistFilter, artistId, eventFilter, eventId, request);
        List<LocationDTO> list = locationMapper.locationToLocationDTO(result.getContent());
        int pageTotal = result.getTotalPages();
        return new PaginationWrapper<>(list, pageTotal);
    }
}
