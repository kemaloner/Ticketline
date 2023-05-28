package at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets;

import at.ac.tuwien.inso.sepm.ticketline.client.TicketlineClientApplication;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.*;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;


@Component
public class TicketsController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketsController.class);

    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<TicketDTO> tableTickets;
    @FXML
    private TableColumn<TicketDTO, String> tcResNr;
    @FXML
    private TableColumn<TicketDTO, String> tcCustomerName;
    @FXML
    private TableColumn<TicketDTO, String> tcPerformanceTitle;
    @FXML
    private TableColumn<TicketDTO, String> tcDate;
    @FXML
    private TableColumn<TicketDTO, String> tcPrice;
    @FXML
    private TableColumn<TicketDTO, String> tcStatus;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnBuy;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnRefresh;

    private static final Integer NUMBER_OF_TICKET_PRO_TABLE = 16;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    private MainController mainController;

    private LocalizationSubject localizationSubject;

    private TicketService ticketService;

    private final SpringFxmlLoader springFxmlLoader;

    private InvoiceService invoiceService;

    private JavaOpenFile javaOpenFile;

    private Sort sort = Sort.by(Sort.Direction.DESC, "dateOfIssue");

    private Task<PaginationWrapper<TicketDTO>> task;

    public TicketsController(TabHeaderController tabHeaderController, SpringFxmlLoader springFxmlLoader,
                             MainController mainController, LocalizationSubject localizationSubject,
                             TicketService ticketService, InvoiceService invoiceService,
                             JavaOpenFile javaOpenFile) {
        this.tabHeaderController = tabHeaderController;
        this.springFxmlLoader = springFxmlLoader;
        this.mainController = mainController;
        this.localizationSubject = localizationSubject;
        this.ticketService = ticketService;
        this.invoiceService = invoiceService;
        this.javaOpenFile = javaOpenFile;
    }

    @FXML
    private void initialize() {

        LOGGER.info("Initalize TicketsController");

        localizationSubject.attach(this);
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(text("tickets.header"));
        btnSearch.setGraphic(fontAwesome.create("SEARCH"));
        btnRefresh.setGraphic(fontAwesome.create("REFRESH"));
        tableTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTickets.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());

        Map<TableColumn, String> columnNamesTicket = new HashMap<>(){{
            put(tcResNr, "reservationNumber");
            put(tcCustomerName, "customer");
            put(tcPerformanceTitle, "performance");
            put(tcDate, "dateOfIssue");
            put(tcPrice, "price");
            put(tcStatus, "status");
        }};

        tableTickets.setOnSort(e -> {
            if(tableTickets.getSortOrder().isEmpty()) { return; }
            TableColumn tc = (TableColumn) tableTickets.getSortOrder().get(0);
            if (columnNamesTicket.containsKey(tc)) {
                sort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, columnNamesTicket.get(tc));
                setUpPagination();
            }
        });
        columnNamesTicket.forEach((c, s) -> c.setCellValueFactory(new PropertyValueFactory<>(s)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        tcPerformanceTitle.setCellValueFactory(v ->  new SimpleStringProperty(v.getValue().getPerformance().getEvent().getTitle()));
        tcCustomerName.setCellValueFactory(v ->  new SimpleStringProperty(v.getValue().getCustomer().getFirstname() +
            " " + v.getValue().getCustomer().getSurname()));
        tcDate.setCellValueFactory(v ->  new SimpleStringProperty(v.getValue().getDateOfIssue().format(formatter)));
        tcStatus.setCellValueFactory(v ->  new SimpleStringProperty(v.getValue().getStatus() == TicketStatus.R ?
            text("ticket.reserve") : v.getValue().getStatus() == TicketStatus.S ? text("ticket.sold")
            : v.getValue().getStatus() == TicketStatus.IC ? text("ticket.invoice.cancelled") :  text("ticket.reservation.cancelled")));
        tcPrice.setCellValueFactory(v ->  new SimpleStringProperty(String.format("%.2f€", v.getValue().getPrice())));

        tableTickets.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                TicketDTO ticketDTO = tableTickets.getSelectionModel().getSelectedItem();
                btnBuy.setDisable(ticketDTO.getStatus() != TicketStatus.R);
                btnCancel.setDisable(ticketDTO.getStatus() != TicketStatus.R && ticketDTO.getStatus() != TicketStatus.S);
                btnPrint.setDisable(ticketDTO.getStatus() != TicketStatus.S && ticketDTO.getStatus() != TicketStatus.IC);
            }
            else{
                btnBuy.setDisable(true);
                btnCancel.setDisable(true);
                btnPrint.setDisable(true);
            }
        });
    }

    @Override
    public void update() {

        tabHeaderController.setTitle(text("tickets.header"));
        tfSearch.setPromptText(text("ticket.search"));
        tcResNr.setText(text("ticket.resNr"));
        tcCustomerName.setText(text("ticket.customerName"));
        tcPerformanceTitle.setText(text("ticket.PerformanceTitle"));
        tcDate.setText(text("ticket.date"));
        tcPrice.setText(text("ticket.price"));
        btnPrint.setText(text("button.print"));
        btnBuy.setText(text("button.buy"));
        btnCancel.setText(text("button.cancellation"));
        tableTickets.refresh();
    }

    private TableView loadTicketsTable(Integer pageIndex){

        LOGGER.info("Load Tickets Table for page: " + pageIndex);

        String keyword = tfSearch.getText();
        if (task!=null && task.isRunning()){ task.cancel(); }
        task = new Task<>() {
            @Override
            protected PaginationWrapper<TicketDTO> call() throws DataAccessException {
                tableTickets.getItems().clear();
                PageRequest request = PageRequest.of(pageIndex, NUMBER_OF_TICKET_PRO_TABLE, sort);
                return keyword.isEmpty() ? ticketService.findAll(request) : ticketService.findByKeyword(keyword, request);
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Ticket List Succesfully created");
                super.succeeded();
                tableTickets.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());
            }

            @Override
            protected void failed() {
                LOGGER.debug("Ticket List can not be created");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), tableTickets.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
        return tableTickets;
    }

    void setUpPagination() {
        pagination.setPageFactory(this::loadTicketsTable);
    }

    public void onTabOpen(){
        setUpPagination();
    }

    public void onTabClose(){

    }

    @FXML
    void onClickedBuy() {
        LOGGER.info("OnClickedBuy: to buy Reserved Tickets");
        TicketDTO ticketDTO = tableTickets.getSelectionModel().getSelectedItem();
        SpringFxmlLoader.Wrapper<HallPlanController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/tickets/hallPlanComponent.fxml");
        //Node node = mainController.eventsTab.getContent();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(TicketlineClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
        stage.setTitle(text("buy.reserve.tickets"));
        wrapper.getController().load(ticketDTO, stage::close, stage);
        stage.setScene(new Scene((Parent) wrapper.getLoadedObject()));
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    void onClickedPrint() {

        LOGGER.info("OnClickedPrint: Invoice printing ");

        TicketDTO ticketDTO = tableTickets.getSelectionModel().getSelectedItem();

        if(ticketDTO == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(BundleManager.getExceptionBundle().getString("noticket.selected"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("error"));
            alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
            alert.show();
            return;
        }

        if(!ticketDTO.getStatus().equals(TicketStatus.S) && !ticketDTO.getStatus().equals(TicketStatus.IC)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(BundleManager.getExceptionBundle().getString("invoice.not.available"));
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("error"));
            alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
            alert.show();
        }else{
            //find invoice by ticketid
            Task<String> task = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return invoiceService.findByTicketId(ticketDTO.getId());
                }

                @Override
                protected void succeeded(){
                    super.succeeded();
                    try {
                        javaOpenFile.open(this.getValue());
                    } catch (IOException e1) {
                        JavaFXUtils.createExceptionDialog(e1,tfSearch.getScene().getWindow()).showAndWait();
                    }
                }

                @Override
                protected void failed(){
                    super.failed();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(this.getMessage());
                    alert.show();
                }
            };

            new Thread(task).start();
        }

    }


    @FXML
    void onClickedSearch() {
        setUpPagination();
    }

    @FXML
    void onClickedCancel() {

        TicketDTO ticketDTO = tableTickets.getSelectionModel().getSelectedItem();

        if(ticketDTO.getPerformance().getStartDateTime().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(text("error"));
            alert.setHeaderText(text("error"));
            alert.setContentText(text("ticket.cannotcancel"));
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(text("confirmation"));
        alert.setHeaderText(text("confirmation"));
        alert.setContentText(text("ticket.canceled"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get() == ButtonType.OK) {

                LOGGER.info("OnClickedCancel: Cancelation buyed or Reserved Ticked");

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws DataAccessException {
                        ticketService.cancelTicket(ticketDTO);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        LOGGER.info("Ticket succesfully canceled");

                        if (ticketDTO.getStatus() == TicketStatus.R) {
                            ticketDTO.setStatus(TicketStatus.RC);
                            btnBuy.setDisable(true);
                            btnCancel.setDisable(true);
                        } else {
                            ticketDTO.setStatus(TicketStatus.IC);
                            btnCancel.setDisable(true);
                        }

                        tableTickets.refresh();
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        JavaFXUtils.createExceptionDialog(getException(), tableTickets.getScene().getWindow()).showAndWait();
                    }
                };
                task.runningProperty().addListener((observable, oldValue, running) ->
                    mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
                );
                new Thread(task).start();
            }
        }

    }

    @FXML
    void onKeyReleasedSearchField(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            onClickedSearch();
        }
    }

    @FXML
    public void refresh(){
        tfSearch.setText("");
        setUpPagination();
    }

}
