package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/artist")
@Api(value = "artist")
public class ArtistEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistEndpoint.class);

    private final ArtistMapper artistMapper;
    private final ArtistService artistService;

    public ArtistEndpoint(ArtistMapper artistMapper, ArtistService artistService){
        this.artistMapper = artistMapper;
        this.artistService = artistService;
    }

    @RequestMapping(value = "/find/byId/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find one artist entry by id")
    public ArtistDTO findOneById(@PathVariable("id") Long id){
        LOGGER.info("Finding an artist by id");
        return artistMapper.artistToArtistDTO(artistService.findOneById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Find every artist entry")
    public PaginationWrapper<ArtistDTO> findAll(Pageable request)
    {
        LOGGER.info("Loading all artists");
        Page<Artist> artists = artistService.findAll(request);
        return new PaginationWrapper<ArtistDTO>(artistMapper.artistToArtistDTO(artists.getContent()), artists.getTotalPages());
    }


    @RequestMapping(value = "/find/advanced", method = RequestMethod.GET)
    @ApiOperation(value = "Find artists by optional parameters")
    public PaginationWrapper<ArtistDTO> findAdvanced(@RequestParam HashMap<String, String> params, Pageable request)
    {
        LOGGER.info("Loading artists by filter");
        boolean isFirstnameGiven, isSurnameGiven, isEventIdGiven;
        String firstname, surname;
        isFirstnameGiven = false;
        isSurnameGiven = false;
        isEventIdGiven = false;
        firstname = "";
        surname = "";
        Long eventId = -1L;

        if(params.containsKey("artistFirstname")){
            isFirstnameGiven = true;
            firstname = params.get("artistFirstname");
        }

        if(params.containsKey("artistSurname")){
            isSurnameGiven = true;
            surname = params.get("artistSurname");
        }

        if(params.containsKey("artistEventId")){
            isEventIdGiven = true;
            eventId = Long.parseLong(params.get("artistEventId"));
        }

        Page<Artist> artists = artistService.findAdvanced(isFirstnameGiven, firstname, isSurnameGiven, surname, isEventIdGiven, eventId, request);
        return new PaginationWrapper<ArtistDTO>(artistMapper.artistToArtistDTO(artists.getContent()), artists.getTotalPages());
    }


}
