package at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallPlanService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;

@Component
public class HallPlanController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(HallPlanController.class);

    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    private AnchorPane hallPlan;
    @FXML
    private ScrollPane hallPlanWrap;
    @FXML
    private ImageView hallPlanImage;
    @FXML
    private AnchorPane seatsPane;
    @FXML
    private VBox vBoxColors;
    @FXML
    private TableView<TicketTableRow> tableTickets;
    @FXML
    private TableColumn<TicketTableRow, String> tcTicket;
    @FXML
    private TableColumn<TicketTableRow, String> tcPrice;
    @FXML
    private Label labelTicketsCountName;
    @FXML
    private Label labelTicketsCountValue;
    @FXML
    private Label labelTotalPriceName;
    @FXML
    private Label labelTotalPriceValue;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnBack;

    private Stage stage;

    private Runnable goToBack;
    private Point2D pressed = new Point2D(0,0);
    private List<SimpleTicketSeatDTO> ticketSeatDTOList = new ArrayList<>();
    private PerformanceDTO performanceDTO;
    private TicketDTO ticketDTO;
    private Map<Integer, List<SimpleTicketSeatDTO>> standSectors = new HashMap<>();
    private Map<Integer, List<SimpleTicketSeatDTO>> blankStandSeats = new HashMap<>();
    private Map<Integer, Integer> selectedSectors = new HashMap<>();
    private List<SimpleTicketSeatDTO> selectedSeats = new ArrayList<>();
    private Map<Double, Integer> multiplierColors = new HashMap<>();
    private List<SimpleTicketSeatDTO> formerlySelectedSeats;
    private Double[] multipliers;
    private static final int[][] colorIndexes = {
        {1},
        {1,10},
        {1,7,10},
        {1,7,8,10},
        {1,7,8,10,13},
        {1,4,7,8,10,13},
        {1,4,6,7,8,10,13},
        {1,3,4,6,7,8,10,13},
        {1,3,4,6,7,8,10,11,13},
        {1,3,4,6,7,8,10,11,12,13},
        {1,3,4,6,7,8,9,10,11,12,13},
        {1,3,4,5,6,7,8,9,10,11,12,13},
        {1,2,3,4,5,6,7,8,9,10,11,12,13}
    };

    private MainController mainController;

    private SpringFxmlLoader springFxmlLoader;

    private LocalizationSubject localizationSubject;

    private HallPlanService hallPlanService;


    public HallPlanController(MainController mainController, SpringFxmlLoader springFxmlLoader,
                              LocalizationSubject localizationSubject, HallPlanService hallPlanService){
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.localizationSubject = localizationSubject;
        this.hallPlanService = hallPlanService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Hall plan controller initialized");
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(text("hallplan.title"));
        hallPlan.setOnMousePressed(event -> {
            hallPlan.setCursor(Cursor.CLOSED_HAND);
            pressed = new Point2D(event.getSceneX(), event.getSceneY());
        });
        hallPlan.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - pressed.getX();
            double deltaY = event.getSceneY() - pressed.getY();
            hallPlan.relocate(hallPlan.getLayoutX() + deltaX, hallPlan.getLayoutY() + deltaY);
            pressed = new Point2D(event.getSceneX(), event.getSceneY());
        });
        hallPlan.setOnMouseReleased(event -> hallPlan.setCursor(Cursor.OPEN_HAND));
        hallPlan.setCursor(Cursor.OPEN_HAND);
        hallPlanWrap.setOnScroll(event -> zoom(event.getDeltaY()>0 ? 1.1 : 0.9));
        hallPlanWrap.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)){
                zoom();
            }
        });

        btnNext.setText(text("button.next"));
        btnBack.setText(text("button.back"));
        tableTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcTicket.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @Override
    public void update() {
        btnNext.setText(text("button.next"));
        btnBack.setText(text("button.back"));
        tabHeaderController.setTitle(text("hallplan.title"));
        labelTicketsCountName.setText(text("hallplan.tickets") + " :");
        labelTotalPriceName.setText(text("hallplan.total_price") + " :");
        tcTicket.setText(text("hallplan.ticket"));
        tcPrice.setText(text("hallplan.price"));
        loadBackgroundImage();
        updateTicketTable();
        drawColorElements();
    }

    private void clear(){
        localizationSubject.detach(this);
        seatsPane.getChildren().forEach(node -> localizationSubject.detach((LocalizationObserver) node));
        ticketSeatDTOList = null;
        performanceDTO = null;
        ticketDTO = null;
        standSectors.clear();
        blankStandSeats.clear();
        selectedSectors.clear();
        selectedSeats.clear();
        multiplierColors.clear();
        multipliers = null;
        seatsPane.getChildren().clear();
        vBoxColors.getChildren().clear();
        tableTickets.getItems().clear();
        formerlySelectedSeats = null;
        hallPlanImage.setImage(null);
    }

    public void load(PerformanceDTO performanceDTO, Runnable goToBack, Stage stage) {
        clear();
        this.performanceDTO = performanceDTO;
        this.goToBack = goToBack;
        this.stage = stage;
        setOnCloseRequest();
        load();
    }

    public void load(TicketDTO ticketDTO, Runnable goToBack, Stage stage) {
        clear();
        this.ticketDTO = ticketDTO;
        this.performanceDTO = ticketDTO.getPerformance();
        this.goToBack = goToBack;
        this.stage = stage;
        setOnCloseRequest();
        load();
    }

    private void setOnCloseRequest(){
        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(text("confirmation"));
            alert.setHeaderText(text("confirmation"));
            alert.setContentText(text("ticket.close.request"));
            Optional<ButtonType> selection = alert.showAndWait();
            if(selection.isPresent()){
                if(selection.get() == ButtonType.OK){
                    if(ticketDTO != null){
                        try {
                            hallPlanService.save(ticketDTO, formerlySelectedSeats);
                        } catch (SeatSelectionException | DataAccessException e1) {
                            JavaFXUtils.createExceptionDialog(e1, stage);
                        }
                    }
                    stage.close();
                }else{
                    e.consume();
                }
            }else{
                e.consume();
            }
        });
    }

    private void load(){
        localizationSubject.attach(this);
        loadBackgroundImage();
        loadSeats();

    }

    private void loadSeats(){
        LOGGER.info("loading Seats");
        Task<List<SimpleTicketSeatDTO>> task = new Task<>() {
            @Override
            protected List<SimpleTicketSeatDTO> call() throws DataAccessException {
                return hallPlanService.findTicketSeatByPerformanceId(performanceDTO.getId());
            }

            @Override
            protected void succeeded() {
                LOGGER.info("loading Seats successful");
                super.succeeded();
                ticketSeatDTOList = getValue();
                drawSeats();
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading Seats failed");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), hallPlan.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void goToBack(){
        //mainController.deleteTemporaryTicket();
        goToBack.run();
        clear();
    }

    private void goToNext(){

        if(getSelectedSeats().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(text("info"));
            alert.setTitle(text("info"));
            alert.setContentText(text("seat.selection.none"));
            alert.show();
            return;
        }

        LOGGER.info("loading Background Image");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws DataAccessException,SeatSelectionException {
                if (ticketDTO!=null){
                   hallPlanService.save(ticketDTO,getSelectedSeats());
                   ticketDTO = hallPlanService.getSavedTicket();
                }
                else {
                    hallPlanService.save(performanceDTO,getSelectedSeats());
                }
                return null;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                if (ticketDTO!=null){ //From Reservation
                    SpringFxmlLoader.Wrapper<TicketDetailController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/tickets/ticketsDetailComponent.fxml");

                    wrapper.getController().load(ticketDTO, formerlySelectedSeats, goToHereInStage()
                    , stage);
                    stage.setScene(new Scene((Parent) wrapper.getLoadedObject()));
                }
                else { //new Ticket
                    SpringFxmlLoader.Wrapper<CustomersController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/customers/customersComponent.fxml");

                    CustomersController controller = wrapper.getController();

                    controller.setUpPagination();
                    controller.load(hallPlanService.getSavedTicket(), goToHereInStage(), stage);
                    stage.setScene(new Scene((Parent) wrapper.getLoadedObject()));
                }
            }
            @Override
            protected void failed() {
                super.failed();
                //refresh
                TicketDTO ticketCopy = ticketDTO;
                PerformanceDTO performanceCopy = performanceDTO;
                clear();
                if(ticketCopy != null){
                    load(ticketCopy, goToBack, stage);
                }else{
                    load(performanceCopy, goToBack, stage);
                }
                tableTickets.getItems().clear();
                JavaFXUtils.createExceptionDialog(getException(),hallPlan.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }


    private void loadBackgroundImage(){
        LOGGER.info("loading Background Image");
        Task<File> task = new Task<>() {
            @Override
            protected File call() throws DataAccessException {
                return hallPlanService.getBackgroundImage(performanceDTO.getHall().getId(),
                    BundleManager.getLocale().getLanguage());
            }
            @Override
            protected void succeeded() {
                LOGGER.info("loaded Background Image");
                super.succeeded();
                setBackground(new Image(getValue().toURI().toString()));
            }
            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),hallPlan.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void setBackground(Image image){
        LOGGER.info("setBackground {}x{},{}", image.getWidth(), image.getHeight(), image.getUrl());
        hallPlanImage.setImage(image);
        hallPlanImage.setFitWidth(image.getWidth());
        hallPlanImage.setFitHeight(image.getHeight());
        hallPlan.setPrefWidth(image.getWidth());
        hallPlan.setPrefHeight(image.getHeight());
        zoom();
    }

    private void zoom(double factor){
        LOGGER.info("zoom {}", factor);
        hallPlan.setScaleX(hallPlan.getScaleX() * factor);
        hallPlan.setScaleY(hallPlan.getScaleY() * factor);
    }

    private void zoom(){
        LOGGER.info("zoom reset");
        hallPlan.setScaleX(1);
        hallPlan.setScaleY(1);
        Image img = hallPlanImage.getImage();
        double factor = Math.min(hallPlanWrap.getWidth()/img.getWidth(), hallPlanWrap.getHeight()/img.getHeight());
        LOGGER.info("hallPlanWrap {}x{}", hallPlanWrap.getWidth(), hallPlanWrap.getHeight());
        LOGGER.info("hallPlanImage {}x{}", hallPlanImage.getImage().getWidth(), hallPlanImage.getImage().getHeight());
        LOGGER.info("factor {}", factor);
        if(factor<1) {
            zoom(factor);
            hallPlan.setLayoutX((img.getWidth() - hallPlanWrap.getWidth())/-2);
            hallPlan.setLayoutY((img.getHeight() - hallPlanWrap.getHeight())/-2);
        }
    }

    private void drawSeats(){
        LOGGER.info("drawSeats");
        multiplierColors = new HashMap<>();
        List<SimpleTicketSeatDTO> seatsAsTypeSeat = new ArrayList<>();
        List<Double> multiplierList = new ArrayList<>();

        for(SimpleTicketSeatDTO seatDTO : ticketSeatDTOList){
            if(seatDTO.getType().equals("STAND")){
                if(!standSectors.containsKey(seatDTO.getSector())){
                    standSectors.put(seatDTO.getSector(), new ArrayList<>());
                    blankStandSeats.put(seatDTO.getSector(), new ArrayList<>());
                }
                standSectors.get(seatDTO.getSector()).add(seatDTO);
                if (seatDTO.getTicketId() == null){
                    blankStandSeats.get(seatDTO.getSector()).add(seatDTO);
                }
            }
            else {
                seatsAsTypeSeat.add(seatDTO);
            }
            if(!multiplierList.contains(seatDTO.getMultiplier())){
                multiplierList.add(seatDTO.getMultiplier());
            }
        }
        multipliers = multiplierList.toArray(new Double[multiplierList.size()]);
        Arrays.sort(multipliers);

        for (int i = 0; i < multipliers.length; i++) {
            multiplierColors.put(multipliers[i], colorIndexes[multipliers.length-1][i]);
        }

        for (Map.Entry<Integer, List<SimpleTicketSeatDTO>> entry : standSectors.entrySet()){
            SimpleTicketSeatDTO dto = entry.getValue().get(0);
            Integer selected = 0;
            Integer blank = entry.getValue().size();
            Integer total = entry.getValue().size();
            Integer colorCat = multiplierColors.get(dto.getMultiplier());
            for(SimpleTicketSeatDTO ticketSeatDTO : entry.getValue()){
                if(ticketSeatDTO.getTicketId() != null){
                    blank--;
                    if(ticketDTO!=null && ticketSeatDTO.getTicketId().equals(ticketDTO.getId())) {
                        selected++;
                    }
                }
            }
            if(selected>0) {
                selectedSectors.put(dto.getSector(), selected);
            }
            SectorElement sectorElement = new SectorElement(dto, selected, blank, total, colorCat, this);
            seatsPane.getChildren().add(sectorElement);
            localizationSubject.attach(sectorElement);
        }

        for (SimpleTicketSeatDTO seatDTO : seatsAsTypeSeat){
            SeatElement seatElement = new SeatElement(seatDTO, multiplierColors.get(seatDTO.getMultiplier()), this);
            if (seatDTO.getTicketId() != null) {
                boolean isSelected = ticketDTO!=null && seatDTO.getTicketId().equals(ticketDTO.getId());
                seatElement.setStatus(isSelected ? SeatElement.Status.SELECTED : SeatElement.Status.FULL);
                if (isSelected){
                    selectedSeats.add(seatDTO);
                }
            }
            seatsPane.getChildren().add(seatElement);
            localizationSubject.attach(seatElement);
        }

        updateTicketTable();
        drawColorElements();

        if(formerlySelectedSeats == null){
            formerlySelectedSeats = getSelectedSeats();
        }

    }

    private void drawColorElements(){
        if (multipliers == null){ return; }
        vBoxColors.getChildren().clear();
        vBoxColors.getChildren().add(new ColorElement("full",text("hallplan.full")));
        vBoxColors.getChildren().add(new ColorElement("selected",text("hallplan.selected")));
        for (Double multiplier : multipliers) {
            String style = "cat" + multiplierColors.get(multiplier);
            String text = String.format("%.2f€", multiplier * performanceDTO.getBasePrice());
            vBoxColors.getChildren().add(new ColorElement(style, text));
        }
    }

    private void selectSeat(SimpleTicketSeatDTO seatDTO, boolean isSelect){
        if(isSelect){
            selectedSeats.add(seatDTO);
        }
        else {
            selectedSeats.remove(seatDTO);
        }
        updateTicketTable();
    }

    private void selectSector(Integer sector, Integer value){
        if(value>0) {
            selectedSectors.put(sector, value);
        } else selectedSectors.remove(sector);
        updateTicketTable();
    }

    private List<SimpleTicketSeatDTO> getSelectedSeats(){
        List<SimpleTicketSeatDTO> list = new ArrayList<>(selectedSeats);
        selectedSectors.forEach((s, v) -> list.addAll(blankStandSeats.get(s).subList(0, v)));
        return list;
    }

    private void updateTicketTable(){
        tableTickets.getItems().clear();
        String seat = text("hallplan.seat");
        String sector = text("hallplan.sector");
        int ticket_count = 0;
        double total_price = 0;

        for(SimpleTicketSeatDTO dto : selectedSeats){
            double price = dto.getMultiplier() * performanceDTO.getBasePrice();
            total_price += price;
            ticket_count++;
            tableTickets.getItems().add(new TicketTableRow(
                String.format("%s %d/%d/%d", seat, dto.getSector(),dto.getRow(), dto.getNumber()),
                String.format("%.2f€", price)
            ));
        }

        for(Map.Entry<Integer, Integer> entry : selectedSectors.entrySet()){
            SimpleTicketSeatDTO dto = standSectors.get(entry.getKey()).get(0);
            double price = entry.getValue() * dto.getMultiplier() * performanceDTO.getBasePrice();
            total_price += price;
            ticket_count+=entry.getValue();
            tableTickets.getItems().add(new TicketTableRow(
                String.format("%dx %s %d", entry.getValue(), sector, dto.getSector()),
                String.format("%.2f€", price)
            ));
        }
        labelTicketsCountValue.setText(String.format("%d",ticket_count));
        labelTotalPriceValue.setText(String.format("%.2f€", total_price));
    }

    @FXML
    private void onBackClicked() {
        LOGGER.info("Back button clicked");
        goToBack();
    }

    @FXML
    private void onNextClicked() {
        LOGGER.info("Next button clicked");
        goToNext();
    }

    private Runnable goToHereInStage(){
        return () -> {
            if(ticketDTO == null){
                mainController.deleteTemporaryTicketIter(hallPlanService.getSavedTicket());
            }

            SpringFxmlLoader.Wrapper<HallPlanController> hallPlanControllerWrapper =
                springFxmlLoader.loadAndWrap("/fxml/tickets/hallPlanComponent.fxml");

            if(ticketDTO != null){
                try {
                    hallPlanService.save(ticketDTO, formerlySelectedSeats);
                }catch (SeatSelectionException | DataAccessException e){
                    JavaFXUtils.createExceptionDialog(e, stage);
                }
                hallPlanControllerWrapper.getController().load(ticketDTO, goToBack, stage);
            }else {
                hallPlanControllerWrapper.getController().load(performanceDTO, goToBack, stage);
            }

            setOnCloseRequest();
            stage.setScene(new Scene((Parent) hallPlanControllerWrapper.getLoadedObject()));
        };
    }

    public static class TicketTableRow{
        private String name;
        private String value;

        private TicketTableRow(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    private static class ColorElement extends HBox {
        ColorElement(String style, String text){
            Label icon = new Label("-");
            icon.getStyleClass().addAll("circle",style);
            Label textLabel = new Label(text);
            textLabel.getStyleClass().add("textLabel");
            getChildren().addAll(icon, textLabel);
            setSpacing(5);
            setAlignment(Pos.CENTER_LEFT);
        }
    }

    private static class SectorElement extends HBox implements LocalizationObserver {
        private Button btnMinus = new Button("-");
        private Button btnPlus = new Button("+");
        private Label label = new Label();
        private int selected, blank, total;

        private HallPlanController hallPlanController;
        private SimpleTicketSeatDTO dto;

        SectorElement(SimpleTicketSeatDTO dto, int selected, int blank, int total, int cat, HallPlanController hallPlanController) {
            this.dto = dto;
            this.hallPlanController = hallPlanController;
            relocate(dto.getX(),dto.getY());
            setRotate(dto.getAngle());
            this.selected = selected;
            this.blank = blank;
            this.total = total;
            getChildren().addAll(btnMinus,label,btnPlus);
            getStyleClass().addAll("sector","cat"+cat);
            btnMinus.setOnMouseClicked(event -> select(event.isControlDown() ? -10 : -1));
            btnPlus.setOnMouseClicked(event -> select(event.isControlDown() ? 10 : 1));
            update();
        }

        private void select(int a){
            a = Math.max(0, Math.min(blank, selected+a));
            if(a!=selected) {
                selected = a;
                hallPlanController.selectSector(dto.getSector(), selected);
                update();
            }
        }

        @Override
        public void update() {
            label.setTooltip(new Tooltip(String.format(text("hallplan.sector.tooltip"),dto.getSector(),selected,blank,total)));
            label.setText(String.format("%d/%d",selected,blank));
            btnMinus.setDisable(selected==0);
            btnPlus.setDisable(selected==blank);
        }
    }

    private static class SeatElement extends Button implements LocalizationObserver{
        public enum Status {BLANK, FULL, SELECTED}
        private Status status = Status.BLANK;
        private HallPlanController hallPlanController;
        private SimpleTicketSeatDTO dto;
        private double price;
        SeatElement(SimpleTicketSeatDTO dto, int cat, HallPlanController hallPlanController){
            this.dto = dto;
            this.hallPlanController = hallPlanController;
            price = hallPlanController.performanceDTO.getBasePrice() * dto.getMultiplier();
            setText("" + dto.getNumber());
            relocate(dto.getX(), dto.getY());
            setRotate(dto.getAngle());
            setCursor(Cursor.HAND);
            getStyleClass().add("seat");
            getStyleClass().add("cat"+cat);
            setOnMouseClicked(e -> select());
            update();
        }

        public boolean isSelected(){
            return getStatus().equals(Status.SELECTED);
        }

        private void select(){
            if(!getStatus().equals(Status.FULL)) {
                setStatus(isSelected() ? Status.BLANK : Status.SELECTED);
                hallPlanController.selectSeat(this.dto, isSelected());
            }
        }

        public void setStatus(Status status){
            getStyleClass().remove(this.status.toString().toLowerCase());
            getStyleClass().add(status.toString().toLowerCase());
            this.status = status;
        }

        public Status getStatus(){
            return status;
        }

        @Override
        public void update() {
            setTooltip(new Tooltip(String.format(text("hallplan.seat.tooltip"), dto.getSector(),dto.getRow(),dto.getNumber(),price)));
        }
    }
}
