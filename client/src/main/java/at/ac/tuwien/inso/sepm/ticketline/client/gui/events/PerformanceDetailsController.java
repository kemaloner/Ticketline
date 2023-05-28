package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PerformanceDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceDetailsController.class);

    @FXML
    private WebView wvPerformanceArtistList;

    @FXML
    private WebView wvPerformancePrice;

    @FXML
    private WebView wvPerformanceLeftCapacity;

    @FXML
    private WebView wvPerformanceHall;

    @FXML
    private WebView wvPerformanceStartDate;

    @FXML
    private WebView wvPerformanceEndDate;

    @FXML
    private WebView wvEventTitle;

    private WebEngine title;
    private WebEngine startDate;
    private WebEngine endDate;
    private WebEngine leftCapacity;
    private WebEngine price;
    private WebEngine hall;
    private WebEngine artistList;

    private PerformanceDTO performanceDTO;

    @FXML
    private void initialize() {
        LOGGER.info("Performance detail controller initialized");

        title = wvEventTitle.getEngine();
        leftCapacity = wvPerformanceLeftCapacity.getEngine();
        price = wvPerformancePrice.getEngine();
        hall = wvPerformanceHall.getEngine();
        startDate = wvPerformanceStartDate.getEngine();
        endDate = wvPerformanceEndDate.getEngine();
        artistList = wvPerformanceArtistList.getEngine();
    }

    public void setData(PerformanceDTO performanceDTO){
        this.performanceDTO = performanceDTO;
        setContent();
    }

    private void setContent () {

        List<ArtistDTO> artistDTOList = performanceDTO.getArtists();
        StringBuilder artistList = new StringBuilder();
        for (ArtistDTO anArtistDTOList : artistDTOList) {
            artistList.append(anArtistDTOList.getFirstname()).append(" ").append(anArtistDTOList.getSurname()).append('\n');
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm");

        this.artistList.loadContent(artistList.toString());
        title.loadContent(performanceDTO.getEvent().getTitle());
        leftCapacity.loadContent(performanceDTO.getLeftCapacity().toString());
        hall.loadContent(performanceDTO.getHall().getDescription());
        price.loadContent("€ "+performanceDTO.getBasePrice().toString());
        startDate.loadContent(performanceDTO.getStartDateTime().format(dateTimeFormatter));
        endDate.loadContent(performanceDTO.getEndDateTime().format(dateTimeFormatter));
    }

}
