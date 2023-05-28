package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventsController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final SpringFxmlLoader springFxmlLoader;


    @FXML
    private TabHeaderController tabHeaderController;

    @FXML
    public Label lbLocationFilter;

    @FXML
    public Button btnClearAllFilter;

    @FXML
    public GridPane gpLocation;

    @FXML
    public Button btnNext;

    @FXML
    public Label tfArtistFilter;

    @FXML
    public Label tfEventsFilter;

    @FXML
    public ChoiceBox<String> cbTyp;

    @FXML
    public Pagination pagination;

    @FXML
    public Button btEventsFilter;

    @FXML
    public GridPane gpEventsFilter;

    @FXML
    public TextField tfEventsDescription;

    @FXML
    public TextField tfEventsContent;

    @FXML
    public TextField tfEventsDuration;

    @FXML
    public Button btArtistFilter;

    @FXML
    public GridPane gpArtistFilter;

    @FXML
    public TextField tfArtistSurname;

    @FXML
    public TextField tfArtistName;

    @FXML
    public Button btLocationSearch;

    @FXML
    public TextField tfLocationDescription;

    @FXML
    public TextField tfLocationCity;

    @FXML
    public TextField tfLocationStreet;

    @FXML
    public TextField tfLocationPostcode;

    @FXML
    public TextField tfLocationCounrty;

    private ArtistDTO artistDTO = null;
    private LocationDTO locationDTO = null;
    private EventDTO eventDTO = null;

    // Event TABLEVIEW
    private TableView<EventDTO> eventDTOTableView;
    private TableColumn<EventDTO, String> eventTitle;
    private TableColumn<EventDTO, String> eventDescription;
    private TableColumn<EventDTO, LocalDateTime> eventStartDate;
    private TableColumn<EventDTO, LocalDateTime> eventEndDate;
    private TableColumn<EventDTO, String> eventCategory;
    private TableColumn<EventDTO, String> eventDuration;

    // Artist TABLEVIEW
    private TableView<ArtistDTO> artistDTOTableView;
    private TableColumn<ArtistDTO, String> artistName;
    private TableColumn<ArtistDTO, String> artistSurname;

    // Location TABLEVIEW
    private TableView<LocationDTO> locationDTOTableView;
    private TableColumn<LocationDTO, String> locationDescripton;
    private TableColumn<LocationDTO, String> locationStreet;
    private TableColumn<LocationDTO, String> locationCity;
    private TableColumn<LocationDTO, String> locationZip;
    private TableColumn<LocationDTO, String> locationCountry;

    //Filter
    private MultiValueMap<String, String> artistFilter = new LinkedMultiValueMap<>();
    private MultiValueMap<String, String> eventsFilter = new LinkedMultiValueMap<>();
    private MultiValueMap<String, String> locationFilter = new LinkedMultiValueMap<>();


    private EventsTables eventsTables = EventsTables.EVENTS;
    private boolean getAllevent = true;
    private boolean getAllartist = true;
    private Tab currentTab;

    private Task<PaginationWrapper<EventDTO>> eventDTOTask;
    private Task<PaginationWrapper<ArtistDTO>> artistDTOTask;
    private Task<PaginationWrapper<LocationDTO>> locationDTOTask;

    private Sort eventSort = Sort.by(Sort.Direction.ASC, "title");
    private Sort artistSort = Sort.by(Sort.Direction.ASC, "firstname");
    private Sort locationSort = Sort.by(Sort.Direction.ASC, "description");

    private LocalizationSubject localizationSubject;

    private EventService eventService;

    private ArtistService artistService;

    private LocationService locationService;

    private MainController mainController;

    public EventsController(SpringFxmlLoader springFxmlLoader, LocalizationSubject localizationSubject, EventService eventService,
                            ArtistService artistService, LocationService locationService, MainController mainController) {
        this.springFxmlLoader = springFxmlLoader;
        this.localizationSubject = localizationSubject;
        this.eventService = eventService;
        this.artistService = artistService;
        this.locationService = locationService;
        this.mainController = mainController;
    }

    private void setListener(TextField tf){
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                if (!newValue.matches("((\\d*)|(\\d+))")) {
                    tf.setText(oldValue);
                }
            }
        });
    }

    @FXML
    private void initialize() {
        LOGGER.info("Event Controller initialized");

        setListener(tfEventsDuration);
        tabHeaderController.setIcon(FontAwesome.Glyph.CALENDAR);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        btEventsFilter.setGraphic(fontAwesome.create("SEARCH"));
        btLocationSearch.setGraphic(fontAwesome.create("SEARCH"));
        btArtistFilter.setGraphic(fontAwesome.create("SEARCH"));
        btLocationSearch.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.location")));
        btArtistFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.artist")));
        btEventsFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.events")));
        createEventTable();
        createArtistTable();
        createLocationTable();
        setChoiceBox(0);
        localizationSubject.attach(this);

        Map<TableColumn, String> eventColumnNames = new HashMap<>(){{
            put(eventTitle, "title");
            put(eventCategory, "category");
            put(eventDescription, "description");
            put(eventStartDate, "startDate");
            put(eventEndDate, "endDate");
            put(eventDuration, "duration");
        }};

        Map<TableColumn, String> artistColumnNames = new HashMap<>(){{
            put(artistName, "firstname");
            put(artistSurname, "surname");
        }};

        Map<TableColumn, String> locationColumnNames = new HashMap<>(){{
            put(locationDescripton, "description");
            put(locationStreet, "street");
            put(locationCity, "city");
            put(locationZip, "zip");
            put(locationCountry, "country");
        }};

        eventDTOTableView.setOnSort(e -> {
           if(eventDTOTableView.getSortOrder().isEmpty()){return;}
           TableColumn tc = (TableColumn) eventDTOTableView.getSortOrder().get(0);
           if(eventColumnNames.containsKey(tc)){
               eventSort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, eventColumnNames.get(tc));
               setUpPagination();
           }
            if(artistColumnNames.containsKey(tc)){
                artistSort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, artistColumnNames.get(tc));
                setUpPagination();
            }
            if(locationColumnNames.containsKey(tc)){
                locationSort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, locationColumnNames.get(tc));
                setUpPagination();
            }
        });

        eventDTOTableView.setRowFactory( tv -> {
            TableRow<EventDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    eventDetails();
                }
            });
            return row;
        });

    }

    private void setChoiceBox(int index){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(BundleManager.getBundle().getString("events.typ.all"),
            BundleManager.getBundle().getString("events.typ.Concert"),
            BundleManager.getBundle().getString("events.typ.Cinema"),
            BundleManager.getBundle().getString("events.typ.Opera"),
            "Festival",
            "Theater",
            "Musical");
        cbTyp.setItems(observableList);
        cbTyp.setValue(cbTyp.getItems().get(index));
    }

    private TableView loadEventPage(int pageIndex) {
        LOGGER.info("Event table loaded");

        Pageable request = PageRequest.of(pageIndex, 20, eventSort);

        if (eventDTOTask!=null && eventDTOTask.isRunning()){ eventDTOTask.cancel(); }

        eventDTOTask = new Task<>() {
            @Override
            protected PaginationWrapper<EventDTO> call() throws DataAccessException {

                if(getAllevent){
                    if(artistDTO == null){
                        return eventService.findAll(request);
                    }else{
                        return eventService.findByArtistId(artistDTO.getId(), request);
                    }
                }else{
                    if(cbTyp.getValue().equals(cbTyp.getItems().get(0))){
                        eventsFilter.clear();
                    }else{
                        eventsFilter.clear();
                        String typ = cbTyp.getValue();
                        if(cbTyp.getSelectionModel().getSelectedIndex() == 1){
                            typ="Concert";
                        }
                        if(cbTyp.getSelectionModel().getSelectedIndex() == 2){
                            typ="Cinema";
                        }
                        if(cbTyp.getSelectionModel().getSelectedIndex() == 3){
                            typ="Opera";
                        }
                        eventsFilter.remove("eventCategory");
                        eventsFilter.add("eventCategory", typ);
                    }
                    if(artistDTO != null){
                        eventsFilter.remove("eventArtistId");
                        eventsFilter.add("eventArtistId", artistDTO.getId().toString());
                    }
                    if(locationDTO != null){
                        eventsFilter.remove("eventLocationId");
                        eventsFilter.add("eventLocationId",locationDTO.getId().toString());
                    }
                    if(!tfEventsDescription.getText().isEmpty()){
                        eventsFilter.remove("eventTitle");
                        eventsFilter.add("eventTitle", tfEventsDescription.getText());
                    }
                    if(!tfEventsContent.getText().isEmpty()){
                        eventsFilter.remove("eventDescription");
                        eventsFilter.add("eventDescription", tfEventsContent.getText());
                    }
                    if(!tfEventsDuration.getText().isEmpty()){
                        eventsFilter.remove("eventDuration");
                        eventsFilter.add("eventDuration", tfEventsDuration.getText());
                    }

                    return eventService.findByCustomCriteria(eventsFilter, request);
                }
            }

            @Override
            public void succeeded(){
                super.succeeded();
                eventDTOTableView.getItems().clear();
                eventDTOTableView.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());
            }

            @Override
            protected void failed(){
                super.failed();
                eventDTOTableView.setItems(FXCollections.observableArrayList());
                clearFilters();
                JavaFXUtils.createExceptionDialog(getException(), btEventsFilter.getScene().getWindow()).showAndWait();
            }
        };

        new Thread(eventDTOTask).start();

        return eventDTOTableView;
    }

    private void createEventTable() {

        eventDTOTableView = new TableView<>();
        eventDTOTableView.setPrefHeight(475);

        eventDTOTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        eventDTOTableView.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());

        eventTitle = new TableColumn<>();
        eventDescription = new TableColumn<>();
        eventCategory = new TableColumn<>();
        eventStartDate = new TableColumn<>();
        eventEndDate = new TableColumn<>();
        eventDuration = new TableColumn<>();
        eventDuration.setPrefWidth(200);

        eventTitle.setText(BundleManager.getBundle().getString("event.title"));
        eventDescription.setText(BundleManager.getBundle().getString("event.content"));
        eventCategory.setText(BundleManager.getBundle().getString("event.typ"));
        eventStartDate.setText(BundleManager.getBundle().getString("event.startdate"));
        eventEndDate.setText(BundleManager.getBundle().getString("event.enddate"));
        eventDuration.setText(BundleManager.getBundle().getString("event.duration"));

        eventTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        eventStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        setDateFormat(eventStartDate);
        eventEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        setDateFormat(eventEndDate);
        eventDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        eventDTOTableView.getColumns().add(eventTitle);
        eventDTOTableView.getColumns().add(eventStartDate);
        eventDTOTableView.getColumns().add(eventEndDate);
        eventDTOTableView.getColumns().add(eventCategory);

    }

    private void setDateFormat(TableColumn<EventDTO, LocalDateTime> tableColumn){
        tableColumn.setCellFactory((TableColumn<EventDTO, LocalDateTime> column) -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm")));
                    }
                }
            };
        });
    }

    private TableView loadArtistPage(int pageIndex) {
        LOGGER.info("Artist table loaded");

        Pageable request = PageRequest.of(pageIndex, 20, artistSort);

        if (artistDTOTask!=null && artistDTOTask.isRunning()){ artistDTOTask.cancel(); }

        artistDTOTask = new Task<PaginationWrapper<ArtistDTO>>() {
            @Override
            protected PaginationWrapper<ArtistDTO> call() throws Exception {

                if(getAllartist){
                    return artistService.findAll(request);
                }else{
                    if(!tfArtistName.getText().isEmpty()){
                        artistFilter.remove("artistFirstname");
                        artistFilter.add("artistFirstname", tfArtistName.getText());
                    }

                    if(!tfArtistSurname.getText().isEmpty()){
                        artistFilter.remove("artistSurname");
                        artistFilter.add("artistSurname", tfArtistSurname.getText());
                    }

                    return artistService.findAdvanced(artistFilter, request);
                }
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                artistDTOTableView.getItems().clear();
                artistDTOTableView.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());

            }

            @Override
            protected void failed(){
                super.failed();
                artistDTOTableView.setItems(FXCollections.observableArrayList());
                clearFilters();
                JavaFXUtils.createExceptionDialog(getException(), btEventsFilter.getScene().getWindow()).showAndWait();
            }
        };

        new Thread(artistDTOTask).start();
        return artistDTOTableView;

    }

    private void createArtistTable() {

        artistDTOTableView = new TableView<>();
        artistDTOTableView.setPrefHeight(475);;

        artistDTOTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        artistDTOTableView.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());

        artistName = new TableColumn<>();
        artistSurname = new TableColumn<>();

        artistName.setText(BundleManager.getBundle().getString("artist.firstname"));
        artistSurname.setText(BundleManager.getBundle().getString("artist.surname"));

        artistName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        artistSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        artistDTOTableView.getColumns().add(artistName);
        artistDTOTableView.getColumns().add(artistSurname);

    }

    private TableView loadLocationPage(int pageIndex) {
        LOGGER.info("Location table loaded");

        Pageable request = PageRequest.of(pageIndex, 20, locationSort);

        if (locationDTOTask!=null && locationDTOTask.isRunning()){ locationDTOTask.cancel(); }

        locationDTOTask = new Task<PaginationWrapper<LocationDTO>>() {
            @Override
            protected PaginationWrapper<LocationDTO> call() throws Exception {

                if(artistDTO != null ){
                    locationFilter.remove("aristId");
                    locationFilter.add("artistId", artistDTO.getId().toString());
                }
                if(!tfLocationDescription.getText().isEmpty()){
                    locationFilter.remove("description");
                    locationFilter.add("description", tfLocationDescription.getText());
                }
                if(!tfLocationStreet.getText().isEmpty()){
                    locationFilter.remove("street");
                    locationFilter.add("street", tfLocationStreet.getText());
                }
                if(!tfLocationPostcode.getText().isEmpty()){
                    locationFilter.remove("zip");
                    locationFilter.add("zip", tfLocationPostcode.getText());
                }
                if(!tfLocationCity.getText().isEmpty()){
                    locationFilter.remove("city");
                    locationFilter.add("city", tfLocationCity.getText());
                }
                if(!tfLocationCounrty.getText().isEmpty()){
                    locationFilter.remove("country");
                    locationFilter.add("country", tfLocationCounrty.getText());
                }

                return locationService.findByFilter(locationFilter, request);

            }

            @Override
            protected void succeeded(){
                super.succeeded();
                locationDTOTableView.getItems().clear();
                locationDTOTableView.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());
            }

            @Override
            protected void failed(){
                super.failed();
                locationDTOTableView.setItems(FXCollections.observableArrayList());
                clearFilters();
                JavaFXUtils.createExceptionDialog(getException(), btEventsFilter.getScene().getWindow()).showAndWait();
            }
        };

        new Thread(locationDTOTask).start();
        return locationDTOTableView;

    }

    private void createLocationTable() {

        locationDTOTableView = new TableView<>();
        locationDTOTableView.setPrefHeight(475);

        locationDTOTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        locationDTOTableView.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());

        locationDescripton = new TableColumn<>();
        locationStreet = new TableColumn<>();
        locationCity = new TableColumn<>();
        locationZip = new TableColumn<>();
        locationCountry = new TableColumn<>();

        locationDescripton.setText(BundleManager.getBundle().getString("location.description"));
        locationStreet.setText(BundleManager.getBundle().getString("location.street"));
        locationCity.setText(BundleManager.getBundle().getString("location.city"));
        locationZip.setText(BundleManager.getBundle().getString("location.postcode"));
        locationCountry.setText(BundleManager.getBundle().getString("location.country"));

        locationDescripton.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        locationCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        locationZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        locationCountry.setCellValueFactory(new PropertyValueFactory<>("country"));

        locationDTOTableView.getColumns().add(locationDescripton);
        locationDTOTableView.getColumns().add(locationStreet);
        locationDTOTableView.getColumns().add(locationZip);
        locationDTOTableView.getColumns().add(locationCity);
        locationDTOTableView.getColumns().add(locationCountry);

    }


    public void setUpPagination() {

        pagination.setPageFactory(pageIndex -> {
            if(eventsTables.equals(EventsTables.EVENTS)){
                return loadEventPage(pageIndex);
            }
            else if(eventsTables.equals(EventsTables.ARTISTS)){
                return loadArtistPage(pageIndex);
            }
            else{
                return loadLocationPage(pageIndex);
            }
        });
    }

    public void onTabOpen(){
        setUpPagination();
    }

    public void onTabClose(){
        SpringFxmlLoader.Wrapper<EventsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/events/eventsComponent.fxml");
        mainController.eventsTab.setContent(wrapper.getLoadedObject());
    }

    public void OnClickedNext() {

        if(eventsTables == EventsTables.EVENTS){
            LOGGER.info("OnClickedNext - from Events Page to Performances Page");
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));

            if(eventDTOTableView.getSelectionModel().getSelectedItem() != null){
                eventDTO = eventDTOTableView.getSelectionModel().getSelectedItem();
            }else{
                eventDTO = null;
            }

            SpringFxmlLoader.Wrapper<PerformanceController> wrapper = springFxmlLoader.loadAndWrap("/fxml/events/performanceComponent.fxml");
            wrapper.getController().setData(eventDTO,currentTab);
            currentTab.setContent(wrapper.getLoadedObject());
            localizationSubject.detach(this);

        }else if(eventsTables == EventsTables.ARTISTS){
            LOGGER.info("OnClickedNext - load Event list for Artist Filters");
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
            gpEventsFilter.setDisable(false);
            btEventsFilter.setDisable(false);
            eventsTables = EventsTables.EVENTS;
            artistDTO = artistDTOTableView.getSelectionModel().getSelectedItem();
            getAllevent = false;

            if(locationFilter.size() == 0){
                gpLocation.setDisable(false);
                btLocationSearch.setDisable(false);
            }

            setUpPagination();
        }else if(eventsTables == EventsTables.LOCATIONS){
            LOGGER.info("OnClickedNext - Load Event list for Location Filters");
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));

            gpEventsFilter.setDisable(false);
            btEventsFilter.setDisable(false);

            if(artistFilter.size() == 0){
                gpArtistFilter.setDisable(false);
                btArtistFilter.setDisable(false);
            }

            locationDTO = locationDTOTableView.getSelectionModel().getSelectedItem();

            eventsTables = EventsTables.EVENTS;
            getAllevent = false;
            setUpPagination();
        }
    }

    public void OnClickedLocationSearch() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.location"));
        gpEventsFilter.setDisable(true);
        btEventsFilter.setDisable(true);

        gpArtistFilter.setDisable(true);
        btArtistFilter.setDisable(true);

        gpLocation.setDisable(true);
        btLocationSearch.setDisable(true);

        eventsTables = EventsTables.LOCATIONS;
        setUpPagination();
    }


    public void OnClickedFilterArtist() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.artist"));
        gpEventsFilter.setDisable(true);
        btEventsFilter.setDisable(true);

        gpArtistFilter.setDisable(true);
        btArtistFilter.setDisable(true);

        gpLocation.setDisable(true);
        btLocationSearch.setDisable(true);

        eventsTables = EventsTables.ARTISTS;
        getAllartist = false;
        setUpPagination();
    }

    private void clearFilters(){
        gpEventsFilter.setDisable(false);
        btEventsFilter.setDisable(false);
        gpArtistFilter.setDisable(false);
        btArtistFilter.setDisable(false);
        tfEventsDuration.setDisable(false);
        tfEventsContent.setDisable(false);
        tfEventsDescription.setDisable(false);
        tfArtistFilter.setDisable(false);
        tfArtistName.setDisable(false);
        tfArtistSurname.setDisable(false);
        cbTyp.setDisable(false);
        gpLocation.setDisable(false);
        btLocationSearch.setDisable(false);
        tfArtistName.clear();
        tfArtistSurname.clear();
        tfEventsDescription.clear();
        tfEventsContent.clear();
        tfEventsDuration.clear();
        eventsFilter.clear();
        artistFilter.clear();
        locationFilter.clear();
        tfLocationCity.clear();
        tfLocationCounrty.clear();
        tfLocationDescription.clear();
        tfLocationPostcode.clear();
        tfLocationStreet.clear();
        cbTyp.setValue(cbTyp.getItems().get(0));
        eventsTables = EventsTables.EVENTS;
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        artistDTO = null;
        locationDTO = null;
        eventDTO = null;
        getAllevent = true;
    }

    public void OnClickedClearAllFilter() {
        LOGGER.info("Clear filter button clicked");
        clearFilters();
        setUpPagination();
    }

    public void OnClickedEventsFilter() {

        eventsTables = EventsTables.EVENTS;
        getAllevent = false;
        setUpPagination();
    }


    @Override
    public void update() {

        if(eventsTables == EventsTables.EVENTS){
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        }else if(eventsTables == EventsTables.ARTISTS){
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.artist"));
        }else{
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.location"));
        }

        eventTitle.setText(BundleManager.getBundle().getString("events.title"));
        eventDescription.setText(BundleManager.getBundle().getString("events.content"));
        eventCategory.setText(BundleManager.getBundle().getString("event.typ"));
        eventStartDate.setText(BundleManager.getBundle().getString("event.startdate"));
        eventEndDate.setText(BundleManager.getBundle().getString("event.enddate"));

        artistName.setText(BundleManager.getBundle().getString("artist.firstname"));
        artistSurname.setText(BundleManager.getBundle().getString("artist.surname"));

        locationDescripton.setText(BundleManager.getBundle().getString("location.description"));
        locationStreet.setText(BundleManager.getBundle().getString("location.street"));
        locationCity.setText(BundleManager.getBundle().getString("location.city"));
        locationZip.setText(BundleManager.getBundle().getString("location.postcode"));
        locationCountry.setText(BundleManager.getBundle().getString("location.country"));

        lbLocationFilter.setText(BundleManager.getBundle().getString("location.filter"));
        tfArtistFilter.setText(BundleManager.getBundle().getString("search.artist"));
        tfEventsFilter.setText(BundleManager.getBundle().getString("events.filter"));

        tfEventsDescription.setPromptText(BundleManager.getBundle().getString("events.title"));
        tfEventsDuration.setPromptText(BundleManager.getBundle().getString("events.duration"));
        tfEventsContent.setPromptText(BundleManager.getBundle().getString("events.content"));

        tfArtistName.setPromptText(BundleManager.getBundle().getString("artist.firstname"));
        tfArtistSurname.setPromptText(BundleManager.getBundle().getString("artist.surname"));

        tfLocationDescription.setPromptText(BundleManager.getBundle().getString("location.title"));
        tfLocationCounrty.setPromptText(BundleManager.getBundle().getString("location.country"));
        tfLocationPostcode.setPromptText(BundleManager.getBundle().getString("location.postcode"));
        tfLocationStreet.setPromptText(BundleManager.getBundle().getString("location.street"));
        tfLocationCity.setPromptText(BundleManager.getBundle().getString("location.city"));

        btnNext.setText(BundleManager.getBundle().getString("button.next"));
        btLocationSearch.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.location")));
        btArtistFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.artist")));
        btEventsFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.events")));
        btnClearAllFilter.setText(BundleManager.getBundle().getString("button.clearall"));


        setChoiceBox(cbTyp.getSelectionModel().getSelectedIndex());

    }

    public void setTab(Tab tab){
        this.currentTab = tab;
    }

    private void eventDetails() {

        if(eventDTOTableView.getSelectionModel().getSelectedItem() != null){

            EventDTO eventDTO = eventDTOTableView.getSelectionModel().getSelectedItem();

            Stage stage = new Stage();
            SpringFxmlLoader.Wrapper<EventDetailsController> wrapper = springFxmlLoader.loadAndWrap("/fxml/events/eventDetails.fxml");
            EventDetailsController eventDetailsController = wrapper.getController();
            eventDetailsController.setData(eventDTO);
            Parent root = (Parent) wrapper.getLoadedObject();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Event Details");
            stage.showAndWait();
        }

    }
}
