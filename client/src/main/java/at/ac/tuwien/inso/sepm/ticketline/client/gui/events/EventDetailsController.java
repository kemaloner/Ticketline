package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class EventDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDetailsController.class);

    @FXML
    private WebView wvTitle;

    @FXML
    private WebView wvDescription;

    @FXML
    private WebView wvCategory;

    @FXML
    private WebView wvDuration;

    @FXML
    private WebView wvStartDate;

    @FXML
    private WebView wvEndDate;

    private WebEngine title;
    private WebEngine description;
    private WebEngine category;
    private WebEngine duration;
    private WebEngine startDate;
    private WebEngine endDate;

    private EventDTO eventDTO;

    @FXML
    private void initialize() {
        LOGGER.info("Event Details Controller initialized");

        title = wvTitle.getEngine();
        description = wvDescription.getEngine();
        category = wvCategory.getEngine();
        duration = wvDuration.getEngine();
        startDate = wvStartDate.getEngine();
        endDate = wvEndDate.getEngine();

    }

    public void setData(EventDTO eventDTO){
        this.eventDTO = eventDTO;
        setContent();
    }

    private void setContent () {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm");

        title.loadContent(eventDTO.getTitle());
        description.loadContent(eventDTO.getDescription());
        category.loadContent(eventDTO.getCategory());
        duration.loadContent(eventDTO.getDuration().toString()+" min.");
        startDate.loadContent(eventDTO.getStartDate().format(dateTimeFormatter));
        endDate.loadContent(eventDTO.getEndDate().format(dateTimeFormatter));
    }

}
