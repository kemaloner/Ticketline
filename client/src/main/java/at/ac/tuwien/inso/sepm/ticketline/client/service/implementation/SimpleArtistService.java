package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ArtistRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SimpleArtistService implements ArtistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArtistService.class);

    private final ArtistRestClient artistRestClient;

    public SimpleArtistService(ArtistRestClient artistRestClient){
        this.artistRestClient = artistRestClient;
    }

    @Override
    public ArtistDTO findOneById(Long id) throws DataAccessException {
        LOGGER.info("Find an artist by id " + id);
        return artistRestClient.findOneById(id);
    }

    @Override
    public PaginationWrapper<ArtistDTO> findAdvanced(MultiValueMap<String, String> params, Pageable request) throws DataAccessException {
        LOGGER.info("Find artists by filter");
        return artistRestClient.findAdvanced(params, request);
    }

    @Override
    public PaginationWrapper<ArtistDTO> findAll(Pageable request) throws DataAccessException{
        LOGGER.info("Find all artists");
        return artistRestClient.findAll(request);
    }
}
