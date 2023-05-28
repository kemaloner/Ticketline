package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimpleArtistService implements ArtistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArtistService.class);

    private final ArtistRepository artistRepository;

    public SimpleArtistService(ArtistRepository artistRepository) {
        this.artistRepository= artistRepository;
    }

    @Override
    public Artist findOneById(Long id) {
        LOGGER.info("Find an artist by id " + id);
        return artistRepository.findOneById(id);
    }

    @Override
    public Page<Artist> findAdvanced(boolean isFirstnameGiven, String firstname, boolean isSurnameGiven, String surname,
                                     boolean isEventIdGiven, Long eventId, Pageable request){
        LOGGER.info("Find artists by filter");
        return artistRepository.findAdvanced(isFirstnameGiven, firstname, isSurnameGiven, surname, isEventIdGiven, eventId, request);
    }

    @Override
    public Page<Artist> findAll(Pageable request) {
        LOGGER.info("Find all artists");
        return artistRepository.findAll(request);
    }
}
