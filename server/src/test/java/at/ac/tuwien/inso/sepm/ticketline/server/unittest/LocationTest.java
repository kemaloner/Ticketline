package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleLocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class LocationTest {

    private static final Long LOCATION1_ID = 1L;
    private static final String LOCATION1_CITY = "Vienna";
    private static final String LOCATION1_COUNTRY = "Austria";
    private static final String LOCATION1_DESCRIPTION = "Lugner City";
    private static final Integer LOCATION1_HOUSENUMBER = 20;
    private static final String LOCATION1_STREET = "Burggasse";
    private static final String LOCATION1_ZIP = "1150";

    private static final Long LOCATION2_ID = 2L;
    private static final String LOCATION2_CITY = "Vienna";
    private static final String LOCATION2_COUNTRY = "Austria";
    private static final String LOCATION2_DESCRIPTION = "Millenium City";
    private static final Integer LOCATION2_HOUSENUMBER = 102;
    private static final String LOCATION2_STREET = "Handelski";
    private static final String LOCATION2_ZIP = "1200";

    private Location location1;
    private Location location2;
    private Optional<Location> optionalLocation1;
    private Optional<Location> optionalLocation2;

    private List<Location> locationList;

    @MockBean
    private LocationRepository locationRepository;

    private LocationService locationService;

    @Before
    public void setUp(){
        locationService = new SimpleLocationService(locationRepository);

        location1 = Location.builder()
            .id(LOCATION1_ID)
            .city(LOCATION1_CITY)
            .country(LOCATION1_COUNTRY)
            .description(LOCATION1_DESCRIPTION)
            .houseNumber(LOCATION1_HOUSENUMBER)
            .street(LOCATION1_STREET)
            .zip(LOCATION1_ZIP)
            .build();

        location2 = Location.builder()
            .id(LOCATION2_ID)
            .city(LOCATION2_CITY)
            .country(LOCATION2_COUNTRY)
            .description(LOCATION2_DESCRIPTION)
            .houseNumber(LOCATION2_HOUSENUMBER)
            .street(LOCATION2_STREET)
            .zip(LOCATION2_ZIP)
            .build();

        locationList = new ArrayList<>();
        optionalLocation1 = Optional.of(location1);
        optionalLocation2 = Optional.of(location2);
        locationList.add(location1);
        locationList.add(location2);
    }

    @Test
    public void shouldFindAllLocations(){

        PageRequest page = PageRequest.of(0,5);

        BDDMockito
            .given(locationRepository.findByFilter(false, null, false, null,
                false, null, false, null, false, null, false,
                null, false, null, page))
            .willReturn(new LocationPageStub(Arrays.asList(location1, location2)));

        Page<Location> locationPage = locationService.findByFilter(false, null, false, null,
            false, null, false, null, false, null, false,
            null, false, null, page);

        assertEquals(locationPage.getContent(), new LocationPageStub(Arrays.asList(location1, location2)).getContent());
    }

    @Test
    public void shouldFindSingleLocation(){

        BDDMockito.given(locationRepository.findById(1L)).willReturn(optionalLocation1);

        Location actualLocation = locationService.findById(1L);
        assertEquals(actualLocation, optionalLocation1.get());
    }

    @Test
    public void shouldFindCorrectLocationByCriteria(){

        PageRequest page = PageRequest.of(0,5);

        BDDMockito
            .given(locationRepository.findByFilter(false, null, true, "Handelski",
                false, null, false, null, false, null, false,
                null, false, null, page))
            .willReturn(new LocationPageStub(Arrays.asList(location2)));

        Page<Location> locationPage = locationService.findByFilter(false, null, true, "Handelski",
            false, null, false, null, false, null, false,
            null, false, null, page);

        assertEquals(locationPage.getContent(), new LocationPageStub(Arrays.asList(location2)).getContent());

    }

    private class LocationPageStub extends PageImpl {

        private List<Location> locationList;

        public LocationPageStub(List<Location> locationList){
            super(locationList);
            this.locationList = locationList;
        }

        @Override
        public List<Location> getContent(){
            return this.locationList;
        }
    }

}
