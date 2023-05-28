package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleArtistService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ArtistTest {

    private static final Long ARTIST1_ID = 1L;
    private static final String ARTIST1_FIRSTNAME = "Max";
    private static final String ARTIST1_SURNAME = "Mustermann";

    private static final Long ARTIST2_ID = 2L;
    private static final String ARTIST2_FIRSTNAME = "Christoph";
    private static final String ARTIST2_SURNAME = "Waltz";

    private static final Long ARTIST3_ID = 3L;
    private static final String ARTIST3_FIRSTNAME = "Arnold";
    private static final String ARTIST3_SURNAME = "Schwarzenegger";

    private Artist artist1;
    private Artist artist2;
    private Artist artist3;

    private List<Artist> artistsList;
    private Page<Artist> artistsPage;

    @MockBean
    private ArtistRepository artistRepository;

    private ArtistService artistService;

    @Before
    public void setUp(){
        artistService = new SimpleArtistService(artistRepository);

        artist1 = Artist.builder()
            .id(ARTIST1_ID)
            .firstname(ARTIST1_FIRSTNAME)
            .lastname(ARTIST1_SURNAME)
            .build();

        artist2 = Artist.builder()
            .id(ARTIST2_ID)
            .firstname(ARTIST2_FIRSTNAME)
            .lastname(ARTIST2_SURNAME)
            .build();

        artist3 = Artist.builder()
            .id(ARTIST3_ID)
            .firstname(ARTIST3_FIRSTNAME)
            .lastname(ARTIST3_SURNAME)
            .build();

        artistsList = new ArrayList<>();
        artistsList.add(artist1);
        artistsList.add(artist2);
        artistsList.add(artist3);


    }

    @Test
    public void shouldFindAllArtists(){

        Pageable request = PageRequest.of(0, 5);
        BDDMockito.given(artistRepository.findAll(request)).willReturn(new ArtistPageStub(artistsList));
        Page<Artist> actualArtists = artistService.findAll(request);
        assertEquals(actualArtists.getContent(), new ArtistPageStub(artistsList).getContent());

    }

    @Test
    public void shouldFindSingleArtist(){

        BDDMockito.given(artistRepository.findOneById(1L)).willReturn(artist1);

        Artist actualArtist = artistService.findOneById(1L);
        assertEquals(actualArtist, artist1);
    }

    @Test
    public void shouldFindCorrectArtistByCriteria(){

        BDDMockito
            .given(artistRepository.findAdvanced(true, "ch", true, "tz",
                false, -1L, PageRequest.of(0, 5)))
            .willReturn(new ArtistPageStub(Arrays.asList(artist3)));

        Page<Artist> artists = artistService.findAdvanced(true, "ch", true, "tz",
            false, -1L, PageRequest.of(0, 5));

        assertEquals(artists.getContent(), new ArtistPageStub(Arrays.asList(artist3)).getContent());

    }

    private class ArtistPageStub extends PageImpl{

        private List<Artist> artistsList;
        private int totalPageCount;

        public ArtistPageStub(List<Artist> artistsList){
            super(artistsList);
            this.artistsList = artistsList;
        }

        @Override
        public List<Artist> getContent(){
            return this.artistsList;
        }
    }
}
