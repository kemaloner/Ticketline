package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.TicketlineClientApplication;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets.HallPlanController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.location.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;

@Component
public class PerformanceController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceController.class);
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final SpringFxmlLoader springFxmlLoader;

    @FXML
    public Pagination pagination;

    @FXML
    public Label tfPerformanceFilter;

    @FXML
    public Button btPerformanceFilter;

    @FXML
    public GridPane gpPerformanceFilter;

    @FXML
    public TextField tfPerformancePrice;

    @FXML
    public ChoiceBox<String> cbTimeFilterfrom;

    @FXML
    public ChoiceBox<String> cbTimeFilterto;

    @FXML
    public DatePicker dpPerformanceDate;

    @FXML
    public Label tfArtistFilter;

    @FXML
    public Button btArtistFilter;

    @FXML
    public GridPane gpArtistFilter;

    @FXML
    public TextField tfArtistName;

    @FXML
    public TextField tfArtistSurname;

    @FXML
    public Label lbLocationFilter;

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

    @FXML
    public Button btnBack;

    @FXML
    public Button btnNext;

    @FXML
    public Button btnClearAllFilter;

    @FXML
    public GridPane gpLocation;

    @FXML
    private TabHeaderController tabHeaderController;

    private Task<PaginationWrapper<PerformanceDTO>> performanceDTOTask;
    private Task<PaginationWrapper<ArtistDTO>> artistDTOTask;
    private Task<PaginationWrapper<LocationDTO>> locationDTOTask;

    private LocationDTO locationDTO;
    private ArtistDTO artistDTO;
    private EventDTO eventDTO;

    private EventsTables eventsTables = EventsTables.PERFORMANCE;
    private boolean getAllArtist = true;

    private Tab currentTab;

    //Performance Table
    private TableView<PerformanceDTO> performanceDTOTableView;
    private TableColumn<PerformanceDTO, String> performanceBasePrice;
    private TableColumn<PerformanceDTO, String> performanceEvent;
    private TableColumn<PerformanceDTO, LocalDateTime> performanceStartTime;
    private TableColumn<PerformanceDTO, LocalDateTime> performanceEndTime;
    private TableColumn<PerformanceDTO, String> performanceArtist;
    private TableColumn<PerformanceDTO, String> performanceHall;
    private TableColumn<PerformanceDTO, Integer> performanceRemainCapacity;

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
    private MultiValueMap<String, String> locationFilter = new LinkedMultiValueMap<>();
    private MultiValueMap<String, String> performanceFilter = new LinkedMultiValueMap<>();

    private Sort performanceSort = Sort.by(Sort.Direction.ASC, "start_date_time");
    private Sort artistSort = Sort.by(Sort.Direction.ASC, "firstname");
    private Sort locationSort = Sort.by(Sort.Direction.ASC, "description");

    private LocalizationSubject localizationSubject;

    private PerformanceService performanceService;

    private ArtistService artistService;

    private LocationService locationService;

    public PerformanceController(SpringFxmlLoader springFxmlLoader, LocalizationSubject localizationSubject,
                                 PerformanceService performanceService, ArtistService artistService,
                                 LocationService locationService) {
        this.springFxmlLoader = springFxmlLoader;
        this.localizationSubject = localizationSubject;
        this.performanceService = performanceService;
        this.artistService = artistService;
        this.locationService = locationService;
    }

    @FXML
    public void OnClickedNext() {

        LOGGER.info("OnClickedNext - from Performance to Customers");
        gpPerformanceFilter.setDisable(false);
        btPerformanceFilter.setDisable(false);

        if(eventsTables == EventsTables.PERFORMANCE){
            LOGGER.info("OnClickedNext - from Performance to Customers");

            tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
            PerformanceDTO performanceDTO = null;

            if(performanceDTOTableView.getSelectionModel().getSelectedItem() != null){
                performanceDTO = performanceDTOTableView.getSelectionModel().getSelectedItem();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle(BundleManager.getBundle().getString("dialog.performance.error.title"));
                alert.setHeaderText(BundleManager.getBundle().getString("dialog.performance.error.header"));
                alert.setContentText(BundleManager.getBundle().getString("dialog.performance.error.context"));
                alert.showAndWait();
                return;
            }


            SpringFxmlLoader.Wrapper<HallPlanController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/tickets/hallPlanComponent.fxml");
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(TicketlineClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
            stage.setTitle(text("buy.reserve.tickets"));
            wrapper.getController().load(performanceDTO, stage::close, stage);
            stage.setScene(new Scene((Parent) wrapper.getLoadedObject()));
            stage.show();
            stage.setResizable(false);
        }else if(eventsTables == EventsTables.ARTISTS){
            LOGGER.debug("OnClickedNext - load Performance list for Artist Filters");
            tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
            eventsTables = EventsTables.PERFORMANCE;
            artistDTO = artistDTOTableView.getSelectionModel().getSelectedItem();
            btnBack.setVisible(true);
            btnBack.setVisible(true);

            if(locationFilter.size() == 0){
                gpLocation.setDisable(false);
                btLocationSearch.setDisable(false);
            }

            setUpPagination();
        }else if(eventsTables == EventsTables.LOCATIONS){
            LOGGER.info("OnClickedNext - Load Performance list for Location Filters");
            tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
            eventsTables = EventsTables.PERFORMANCE;
            locationDTO = locationDTOTableView.getSelectionModel().getSelectedItem();

            if(artistFilter.size() == 0){
                gpArtistFilter.setDisable(false);
                btArtistFilter.setDisable(false);
            }

            setUpPagination();
            btnBack.setVisible(true);
        }
    }

    public void setData(EventDTO eventDTO, Tab tab){
        this.currentTab = tab;
        this.eventDTO = eventDTO;
    }



    public void OnClickedBack() {

        LOGGER.info("OnClickedNext - from Performances Page to Events Page");

        SpringFxmlLoader.Wrapper<EventsController> wrapper = springFxmlLoader.loadAndWrap("/fxml/events/eventsComponent.fxml");
        wrapper.getController().setTab(currentTab);
        wrapper.getController().onTabOpen();
        currentTab.setContent(wrapper.getLoadedObject());
        clearAllFilter();
        localizationSubject.detach(this);
    }

    @FXML
    private void initialize() {
        LOGGER.info("Performance Controller initialized");

        tabHeaderController.setIcon(FontAwesome.Glyph.CALENDAR);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
        btPerformanceFilter.setGraphic(fontAwesome.create("SEARCH"));
        btLocationSearch.setGraphic(fontAwesome.create("SEARCH"));
        btArtistFilter.setGraphic(fontAwesome.create("SEARCH"));
        btLocationSearch.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.location")));
        btArtistFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.artist")));
        createPerformanceTable();
        createArtistTable();
        createLocationTable();
        cbTimeFilterfrom = setChoiceBox(0,0,cbTimeFilterfrom);
        cbTimeFilterto = setChoiceBox(0,12,cbTimeFilterto);
        setUpPagination();
        performanceFilter.clear();
        locationFilter.clear();
        artistFilter.clear();
        localizationSubject.attach(this);
        forceDouble();
        dpPerformanceDate.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
            checkDatePicker()
        );
        cbTimeFilterfrom.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
            checkHourPicker()
        );

        Map<TableColumn, String> performanceColumnNames = new HashMap<>(){{
           put(performanceBasePrice, "base_price");
           put(performanceStartTime, "start_date_time");
           put(performanceEndTime, "end_date_time");
           put(performanceRemainCapacity, "left_capacity");

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

        performanceDTOTableView.setOnSort(e -> {
            if (performanceDTOTableView.getSortOrder().isEmpty()){ return; }
            TableColumn tc = performanceDTOTableView.getSortOrder().get(0);
            if(performanceColumnNames.containsKey(tc)){
                performanceSort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, performanceColumnNames.get(tc));
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

        performanceDTOTableView.setRowFactory( tv -> {
            TableRow<PerformanceDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    performanceDetails();
                }
            });
            return row;
        });

    }

    private ChoiceBox<String> setChoiceBox(int index, int startHour, ChoiceBox<String> choiceBox){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add(BundleManager.getBundle().getString("performance.settime"));
        for (int i = startHour; i < 24; i ++){
                observableList.add(String.format("%02d:%02d",i,0));
        }
        choiceBox.setItems(observableList);
        choiceBox.setValue(choiceBox.getItems().get(index));
        return choiceBox;
    }

    private TableView loadPerformancePage(int pageIndex) {

        LOGGER.info("Setting up PerformancePage:" +pageIndex);

        Pageable request = PageRequest.of(pageIndex, 20, performanceSort);

        if(performanceDTOTask != null && performanceDTOTask.isRunning()){performanceDTOTask.cancel();}

        performanceDTOTask = new Task<>() {
            @Override
            protected PaginationWrapper<PerformanceDTO> call() throws DataAccessException {
                performanceDTOTableView.getItems().clear();
                if(!tfPerformancePrice.getText().isEmpty()){
                    performanceFilter.remove("price");
                    performanceFilter.add("price", tfPerformancePrice.getText());
                }

                if(dpPerformanceDate.getValue() != null){
                    performanceFilter.remove("dateTimeFrom");
                    performanceFilter.remove("dateTimeTo");

                    if(cbTimeFilterfrom.getSelectionModel().getSelectedIndex() != 0){
                        LocalDateTime localDateTime = LocalDateTime.of(dpPerformanceDate.getValue().getYear(),
                            dpPerformanceDate.getValue().getMonth(), dpPerformanceDate.getValue().getDayOfMonth(),
                            Integer.parseInt(cbTimeFilterfrom.getValue().substring(0,2)),
                            Integer.parseInt(cbTimeFilterfrom.getValue().substring(3)));
                        performanceFilter.add("dateTimeFrom", String.valueOf(localDateTime));
                    }else{
                        LocalDateTime localDateTime = LocalDateTime.of(dpPerformanceDate.getValue().getYear(),
                            dpPerformanceDate.getValue().getMonth(), dpPerformanceDate.getValue().getDayOfMonth(),
                            0,
                            0);
                        performanceFilter.add("dateTimeFrom", String.valueOf(localDateTime));
                    }

                    if(cbTimeFilterto.getSelectionModel().getSelectedIndex() != 0){
                        LocalDateTime localDateTime = LocalDateTime.of(dpPerformanceDate.getValue().getYear(),
                            dpPerformanceDate.getValue().getMonth(), dpPerformanceDate.getValue().getDayOfMonth(),
                            Integer.parseInt(cbTimeFilterto.getValue().substring(0,2)),
                            Integer.parseInt(cbTimeFilterto.getValue().substring(3)));
                        performanceFilter.add("dateTimeTo", String.valueOf(localDateTime));
                    }else{
                        LocalDateTime localDateTime = LocalDateTime.of(dpPerformanceDate.getValue().getYear(),
                            dpPerformanceDate.getValue().getMonth(), dpPerformanceDate.getValue().getDayOfMonth(),
                            23,
                            59);
                        performanceFilter.add("dateTimeTo", String.valueOf(localDateTime));
                    }
                }

                if(locationDTO != null){
                    performanceFilter.remove("locationId");
                    performanceFilter.add("locationId", locationDTO.getId().toString());
                }

                if(artistDTO != null){
                    performanceFilter.remove("artistId");
                    performanceFilter.add("artistId", artistDTO.getId().toString());
                }

                if(eventDTO != null){
                    performanceFilter.remove("eventId");
                    performanceFilter.add("eventId", eventDTO.getId().toString());
                }

                return performanceService.findByFilter(performanceFilter, request);
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                pagination.setPageCount(getValue().getTotalPages());
                performanceDTOTableView.getItems().clear();
                performanceDTOTableView.getItems().addAll(getValue().getEntities());
            }

            @Override
            protected void failed(){
                super.failed();
                performanceDTOTableView.setItems(FXCollections.observableArrayList());
                clearAllFilter();
                JavaFXUtils.createExceptionDialog(getException(), pagination.getScene().getWindow()).showAndWait();
            }
        };

        new Thread(performanceDTOTask).start();

        return performanceDTOTableView;
    }

    private void createPerformanceTable() {

        performanceDTOTableView = new TableView<>();
        performanceDTOTableView.setPrefHeight(485);

        performanceDTOTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        performanceDTOTableView.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());

        performanceStartTime = new TableColumn<>();
        performanceEndTime = new TableColumn<>();
        performanceBasePrice = new TableColumn<>();
        performanceHall = new TableColumn<>();
        performanceArtist = new TableColumn<>();
        performanceEvent = new TableColumn<>();
        performanceRemainCapacity = new TableColumn<>();

        performanceStartTime.setText(BundleManager.getBundle().getString("performance.starttime"));
        performanceEndTime.setText(BundleManager.getBundle().getString("performance.endtime"));
        performanceBasePrice.setText(BundleManager.getBundle().getString("performance.baseprice"));
        performanceHall.setText(BundleManager.getBundle().getString("performance.hall"));
        performanceArtist.setText(BundleManager.getBundle().getString("performance.artist"));
        performanceEvent.setText(BundleManager.getBundle().getString("performance.event"));
        performanceRemainCapacity.setText(BundleManager.getBundle().getString("performance.leftcapacity"));


        performanceStartTime.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        setDateFormat(performanceStartTime);
        performanceEndTime.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        setDateFormat(performanceEndTime);
        performanceBasePrice.setCellValueFactory(v ->  new SimpleStringProperty(String.format("%.2f€", v.getValue().getBasePrice())));
        performanceHall.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHall().getDescription()));
        performanceArtist.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtists().toString()));
        performanceEvent.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getTitle()));
        performanceRemainCapacity.setCellValueFactory(new PropertyValueFactory<>("leftCapacity"));

        performanceDTOTableView.getColumns().add(0, performanceEvent);
        performanceDTOTableView.getColumns().add(1, performanceStartTime);
        performanceDTOTableView.getColumns().add(2, performanceEndTime);
        performanceDTOTableView.getColumns().add(3, performanceBasePrice);
        performanceDTOTableView.getColumns().add(4, performanceRemainCapacity);
        //performanceDTOTableView.getColumns().addAll(performanceEvent,performanceStartTime,performanceEndTime,performanceBasePrice,performanceRemainCapacity);


    }

    private void setDateFormat(TableColumn<PerformanceDTO, LocalDateTime> tableColumn){
        tableColumn.setCellFactory((TableColumn<PerformanceDTO, LocalDateTime> column) -> {
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

        if(artistDTOTask != null && artistDTOTask.isRunning()){
            artistDTOTask.cancel();
        }

        artistDTOTask = new Task<>() {
            @Override
            protected PaginationWrapper<ArtistDTO> call() throws DataAccessException {
                artistDTOTableView.getItems().clear();
                if(getAllArtist){
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

                    if(eventDTO != null){
                        artistFilter.remove("");
                        artistFilter.add("", eventDTO.getId().toString());
                    }

                    return artistService.findAdvanced(artistFilter, request);
                }
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                pagination.setPageCount(getValue().getTotalPages());
                artistDTOTableView.getItems().clear();
                artistDTOTableView.getItems().addAll(getValue().getEntities());;
            }

            @Override
            protected void failed(){
                super.failed();
                artistDTOTableView.setItems(FXCollections.observableArrayList());
                clearAllFilter();
                JavaFXUtils.createExceptionDialog(getException(), pagination.getScene().getWindow()).showAndWait();
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

        if(locationDTOTask != null && locationDTOTask.isRunning()){
            locationDTOTask.cancel();
        }

        locationDTOTask = new Task<>() {
            @Override
            protected PaginationWrapper<LocationDTO> call() throws DataAccessException {
                locationDTOTableView.getItems().clear();
                if(artistDTO != null ){
                    locationFilter.remove("artistId");
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
                if(eventDTO != null){
                    locationFilter.remove("eventId");
                    locationFilter.add("eventId", eventDTO.getId().toString());
                }

                return locationService.findByFilter(locationFilter, request);
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                pagination.setPageCount(getValue().getTotalPages());
                locationDTOTableView.getItems().clear();
                locationDTOTableView.getItems().addAll(getValue().getEntities());;
            }

            @Override
            protected void failed(){
                super.failed();
                locationDTOTableView.setItems(FXCollections.observableArrayList());
                clearAllFilter();
                JavaFXUtils.createExceptionDialog(getException(), pagination.getScene().getWindow()).showAndWait();
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
            if(eventsTables.equals(EventsTables.PERFORMANCE)){
                return loadPerformancePage(pageIndex);
            }else if(eventsTables.equals(EventsTables.ARTISTS)){
                return loadArtistPage(pageIndex);
            }else{
                return loadLocationPage(pageIndex);
            }
        });
    }

    @Override
    public void update() {

        if(eventsTables == EventsTables.PERFORMANCE){
            tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
        }else if(eventsTables == EventsTables.ARTISTS){
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.artist"));
        }else{
            tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.location"));
        }

        tfPerformancePrice.setPromptText(BundleManager.getBundle().getString("performance.baseprice"));
        dpPerformanceDate.setPromptText(BundleManager.getBundle().getString("performance.date"));
        performanceStartTime.setText(BundleManager.getBundle().getString("performance.starttime"));
        performanceEndTime.setText(BundleManager.getBundle().getString("performance.endtime"));
        performanceBasePrice.setText(BundleManager.getBundle().getString("performance.baseprice"));
        performanceHall.setText(BundleManager.getBundle().getString("performance.hall"));
        performanceArtist.setText(BundleManager.getBundle().getString("performance.artist"));
        performanceEvent.setText(BundleManager.getBundle().getString("performance.event"));
        performanceRemainCapacity.setText(BundleManager.getBundle().getString("performance.leftcapacity"));

        artistName.setText(BundleManager.getBundle().getString("artist.firstname"));
        artistSurname.setText(BundleManager.getBundle().getString("artist.surname"));

        locationDescripton.setText(BundleManager.getBundle().getString("location.description"));
        locationStreet.setText(BundleManager.getBundle().getString("location.street"));
        locationCity.setText(BundleManager.getBundle().getString("location.city"));
        locationZip.setText(BundleManager.getBundle().getString("location.postcode"));
        locationCountry.setText(BundleManager.getBundle().getString("location.country"));

        lbLocationFilter.setText(BundleManager.getBundle().getString("location.filter"));
        tfArtistFilter.setText(BundleManager.getBundle().getString("search.artist"));
        tfPerformanceFilter.setText(BundleManager.getBundle().getString("events.filter"));

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
        btPerformanceFilter.setTooltip(new Tooltip(BundleManager.getBundle().getString("button.filter.events")));

        btnNext.setText(BundleManager.getBundle().getString("button.next"));
        btnBack.setText(BundleManager.getBundle().getString("button.back"));
        btnClearAllFilter.setText(text("button.clearall"));

        cbTimeFilterfrom = setChoiceBox(cbTimeFilterfrom.getSelectionModel().getSelectedIndex(),0,cbTimeFilterfrom);
        cbTimeFilterto = setChoiceBox(cbTimeFilterto.getSelectionModel().getSelectedIndex(), cbTimeFilterfrom.getSelectionModel().getSelectedIndex(),cbTimeFilterto);
    }

    private void clearAllFilter(){
        gpArtistFilter.setDisable(false);
        btArtistFilter.setDisable(false);
        tfArtistFilter.setDisable(false);
        gpLocation.setDisable(false);
        btLocationSearch.setDisable(false);
        lbLocationFilter.setDisable(false);
        gpPerformanceFilter.setDisable(false);
        btPerformanceFilter.setDisable(false);
        tfPerformanceFilter.setDisable(false);
        tfArtistName.clear();
        tfArtistSurname.clear();
        artistFilter.clear();
        locationFilter.clear();
        tfLocationCity.clear();
        tfLocationCounrty.clear();
        tfLocationDescription.clear();
        tfLocationPostcode.clear();
        tfLocationStreet.clear();
        tfPerformancePrice.clear();
        dpPerformanceDate.getEditor().clear();
        dpPerformanceDate.setValue(null);
        cbTimeFilterfrom.setValue(cbTimeFilterfrom.getItems().get(0));
        cbTimeFilterto.setValue(cbTimeFilterto.getItems().get(0));
        locationDTO = null;
        artistDTO = null;
        btnBack.setVisible(true);
        performanceFilter.clear();
        locationFilter.clear();
        artistFilter.clear();
        tabHeaderController.setTitle(text("performance.performance"));
    }

    public void OnClickedClearAllFilter() {
        LOGGER.info("Clear filter button clicked");

        clearAllFilter();
        eventsTables = EventsTables.PERFORMANCE;
        setUpPagination();
    }

    public void OnClickedLocationSearch() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.location"));
        gpPerformanceFilter.setDisable(true);
        btPerformanceFilter.setDisable(true);

        gpArtistFilter.setDisable(true);
        btArtistFilter.setDisable(true);

        gpLocation.setDisable(true);
        btLocationSearch.setDisable(true);

        eventsTables = EventsTables.LOCATIONS;
        btnBack.setVisible(false);
        setUpPagination();
    }

    public void OnClickedFilterArtist() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events.artist"));
        gpPerformanceFilter.setDisable(true);
        btPerformanceFilter.setDisable(true);

        gpArtistFilter.setDisable(true);
        btArtistFilter.setDisable(true);

        gpLocation.setDisable(true);
        btLocationSearch.setDisable(true);

        eventsTables = EventsTables.ARTISTS;
        btnBack.setVisible(false);
        getAllArtist = false;
        setUpPagination();
    }

    public void OnClickedPerformanceFilter() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("performance.performance"));
        eventsTables = EventsTables.PERFORMANCE;
        setUpPagination();
    }

    private void performanceDetails() {

        if(performanceDTOTableView.getSelectionModel().getSelectedItem() != null){

            PerformanceDTO performanceDTO = performanceDTOTableView.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            SpringFxmlLoader.Wrapper<PerformanceDetailsController> wrapper = springFxmlLoader.loadAndWrap("/fxml/events/performanceDetails.fxml");
            PerformanceDetailsController performanceDetailsController = wrapper.getController();
            performanceDetailsController.setData(performanceDTO);
            Parent root = (Parent) wrapper.getLoadedObject();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Performance Details");
            stage.showAndWait();
        }
    }

    private void forceDouble(){
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        tfPerformancePrice.setTextFormatter(formatter);

        TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        tfPerformancePrice.setTextFormatter(formatter2);
    }

    private void checkDatePicker(){
        if(dpPerformanceDate.getValue() != null && dpPerformanceDate.getEditor().getText().isEmpty()){
            dpPerformanceDate.setValue(null);
            cbTimeFilterfrom.setDisable(true);
        }else {
            cbTimeFilterfrom.setDisable(false);
        }
    }

    private void checkHourPicker(){
        if(cbTimeFilterfrom.getSelectionModel().getSelectedIndex() != 0){
            cbTimeFilterto.setDisable(false);
            cbTimeFilterto = setChoiceBox(0, cbTimeFilterfrom.getSelectionModel().getSelectedIndex(),cbTimeFilterto);
        }else{
            cbTimeFilterto.setDisable(true);
        }
    }

}
