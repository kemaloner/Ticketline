package at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.HallPlanService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.*;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;


@Component
public class TicketDetailController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDetailController.class);

    @FXML
    private Label lbEventTitle;

    @FXML
    private Label lbPerformanceStartDate;

    @FXML
    private Label lbPerformanceEndDate;

    @FXML
    private Label lbReservationNumber;

    @FXML
    private Label lbPrice;

    @FXML
    private Label lbTickets;

    @FXML
    private Label lbCustomerName;

    @FXML
    private Label lbCustomerNameValue;

    @FXML
    private Label lbEventTitleValue;

    @FXML
    private Label lbPerformanceStartDateValue;

    @FXML
    private Label lbPerformanceEndDateValue;

    @FXML
    private Label lbReservationNumberValue;

    @FXML
    private Label lbTicketsValue;

    @FXML
    private Label lbPriceValue;

    @FXML
    private Button btCancel;

    @FXML
    private Button btBuy;

    @FXML
    private Button btReservation;

    @FXML
    private TabHeaderController tabHeaderController;

    @FXML
    private Button btnToClipboard;

    @FXML
    private Button btBack;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    private TicketDTO ticketDTO;
    private Runnable goToBack;
    private Stage stage;
    private List<SimpleTicketSeatDTO> formerlySelectedSeats;

    private InvoiceService invoiceService;

    private HallPlanService hallPlanService;

    private JavaOpenFile javaOpenFile;

    private MainController mainController;

    private PerformanceController performanceController;

    private TicketsController ticketsController;

    private TicketService ticketService;

    private LocalizationSubject localizationSubject;

    public TicketDetailController(InvoiceService invoiceService, HallPlanService hallPlanService,
                                  JavaOpenFile javaOpenFile, MainController mainController,
                                  PerformanceController performanceController, TicketsController ticketsController,
                                  TicketService ticketService, LocalizationSubject localizationSubject){

        this.invoiceService = invoiceService;
        this.hallPlanService = hallPlanService;
        this.javaOpenFile = javaOpenFile;
        this.mainController = mainController;
        this.performanceController = performanceController;
        this.ticketsController = ticketsController;
        this.ticketService = ticketService;
        this.localizationSubject = localizationSubject;
    }

    @FXML
    public void initialize(){
        btnToClipboard.setGraphic(fontAwesome.create("CLIPBOARD"));
        btnToClipboard.setTooltip(new Tooltip(BundleManager.getBundle().getString("copy.clipboard")));
    }

    @FXML
    public void onCopyToClipboardClicked(){
        LOGGER.info("Reservation number is copied");
        StringSelection stringSelection = new StringSelection(lbReservationNumberValue.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        playTransition();
    }

    @FXML
    void onActionBuyButtonClicked() {
        LOGGER.info("Buy button clicked");
        //use task
        Task<TicketDTO> task = new Task<>() {

            @Override
            protected TicketDTO call() throws Exception {
                return ticketService.buy(ticketDTO);
            }

            @Override
            protected void succeeded(){
                super.succeeded();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(text("confirmation"));
                alert.setHeaderText(text("confirmation"));
                alert.setContentText(text("buy.success"));
                ticketsController.setUpPagination();
                if(performanceController.pagination != null) {
                    performanceController.setUpPagination();
                }
                stage.close();
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent()){
                    if(result.get() == ButtonType.OK){
                        //print pdf
                        try {
                            javaOpenFile.open(
                                invoiceService.findByTicketId(
                                    ticketDTO.getId()
                                )
                            );
                        } catch (IOException | DataAccessException e1) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText(e1.getMessage());
                            alert.show();
                        }
                    }//else do nothing
                }

                clear();
            }

            @Override
            protected void failed(){
                super.failed();
                JavaFXUtils.createExceptionDialog(this.getException(), btnToClipboard.getScene().getWindow());
            }
        };

        new Thread(task).start();
    }

    @FXML
    void onActionCancelButtonClicked() {
        LOGGER.info("Cancel button clicked");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(text("confirmation"));
        alert.setHeaderText(text("confirmation"));
        if(ticketDTO.getStatus().equals(TicketStatus.T)){
            alert.setContentText(text("ticket.close.request") + "\n" + text("seats.will.be.freed"));
            Optional<ButtonType> selection = alert.showAndWait();
            if(selection.isPresent()){
                if(selection.get() == ButtonType.OK){
                    Task<Void> task = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            ticketService.delete(ticketDTO);
                            return null;
                        }

                        @Override
                        protected void succeeded(){
                            super.succeeded();
                            stage.close();
                            clear();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(text("info"));
                            alert.setTitle(text("info"));
                            alert.setContentText(text("seats.freed"));
                            alert.show();
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


        }
        if(ticketDTO.getStatus().equals(TicketStatus.R)){
            alert.setContentText(text("ticket.close.request"));
            Optional<ButtonType> selection = alert.showAndWait();
            if(selection.isPresent()){
                if(selection.get() == ButtonType.OK){
                    Task<Void> task = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            hallPlanService.save(ticketDTO, formerlySelectedSeats);
                            return null;
                        }

                        @Override
                        protected void succeeded(){
                            super.succeeded();
                            stage.close();
                            clear();
                        }

                        @Override
                        protected void failed(){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText(getException().getMessage());
                            alert.show();
                        }
                    };

                    new Thread(task).start();
                }
            }
        }
    }

    @FXML
    void onActionReservationButtonClicked() {
        LOGGER.info("Reserve button clicked");

        Task<TicketDTO> task = new Task<>() {

            @Override
            protected TicketDTO call() throws Exception {
                return ticketService.reserve(ticketDTO);
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(text("info"));
                alert.setTitle(text("info"));
                alert.setContentText(text("reservation.success"));
                ticketsController.setUpPagination();
                if(performanceController.pagination != null){
                    performanceController.setUpPagination();
                }
                stage.close();
                clear();
                alert.show();
            }

            @Override
            protected void failed(){
                super.failed();
                clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(this.getMessage());
                alert.show();
            }
        };

        new Thread(task).start();
    }

    @FXML
    private void onActionBackButtonClicked() {
        if(goToBack!=null){
            goToBack.run();
            clear();
        }
    }

    private void clear(){
        localizationSubject.detach(this);
        ticketDTO = null;
    }

    public void load(TicketDTO ticketDTO, Runnable goToBack, Stage stage) {
        clear();
        this.ticketDTO = ticketDTO;
        this.goToBack = goToBack;
        this.stage = stage;
        loadTicketInfo();
    }

    public void load(TicketDTO ticketDTO, List<SimpleTicketSeatDTO> formerlySelectedSeats, Runnable goToBack, Stage stage){
        clear();
        this.ticketDTO = ticketDTO;
        this.formerlySelectedSeats = formerlySelectedSeats;
        this.goToBack = goToBack;
        this.stage = stage;
        loadTicketInfo();
    }

    private void loadTicketInfo() {
        localizationSubject.attach(this);
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(text("tickets.header.detail"));
        lbCustomerNameValue.setText(ticketDTO.getCustomer().getFirstname() + " " + ticketDTO.getCustomer().getSurname());
        lbEventTitleValue.setText(ticketDTO.getPerformance().getEvent().getTitle());
        lbPerformanceStartDateValue.setText(ticketDTO.getPerformance().getStartDateTime().toString());
        lbPerformanceEndDateValue.setText(ticketDTO.getPerformance().getEndDateTime().toString());
        lbReservationNumberValue.setText(ticketDTO.getReservationNumber());
        lbPriceValue.setText(String.format("%.2f€",ticketDTO.getPrice()));
        loadTicketCount();
    }

    private void loadTicketCount(){
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws DataAccessException {
                return ticketService.countTicketSeatByTicketId(ticketDTO.getId());
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                lbTicketsValue.setText(String.format("%d",getValue()));
            }
            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),btBuy.getScene().getWindow()).showAndWait();
            }
        };
        new Thread(task).start();
    }


    @Override
    public void update() {
        lbPrice.setText(text("ticket.price"));
        lbTickets.setText(text("ticket.count"));
        lbReservationNumber.setText(text("ticket.resNr"));
        lbPerformanceEndDate.setText(text("performance.endtime"));
        lbPerformanceStartDate.setText(text("performance.starttime"));
        lbEventTitle.setText(text("events.title"));
        lbCustomerName.setText(text("customer.customer"));
        tabHeaderController.setTitle(text("tickets.header.detail"));
        btBuy.setText(text("button.buy"));
        btBack.setText(text("button.back"));
        btCancel.setText(text("button.cancel"));
        btReservation.setText(text("button.reservation"));
        btnToClipboard.setTooltip(new Tooltip(text("copy.clipboard")));
    }

    private void playTransition(){

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/fadeOut.fxml"), BundleManager.getBundle());

            Scene scene = new Scene(root);
            Stage window = new Stage();

            scene.setFill(Color.TRANSPARENT);
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.setAlwaysOnTop(true);
            window.setX(mainController.getvBoxMain().getScene().getWindow().getX() + mainController.getvBoxMain().getScene().getWindow().getWidth()/4);
            window.setY(mainController.getvBoxMain().getScene().getWindow().getY() + mainController.getvBoxMain().getScene().getWindow().getHeight()/12);

            FadeTransition fadeInTransition = new FadeTransition(Duration.millis(2000), root);
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);

            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2000), root);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);

            scene.setOnMouseEntered(event -> {
                fadeInTransition.stop();
                fadeOutTransition.stop();
                root.setOpacity(1);
            });

            scene.setOnMouseExited(event -> {
                fadeOutTransition.play();
            });

            fadeOutTransition.setOnFinished(event -> {
                window.close();
            });

            fadeInTransition.setOnFinished(event -> {
                fadeOutTransition.play();
            });

            window.setOnShowing(event -> {
                fadeInTransition.play();

            });
            window.show();
        } catch (IOException e) {
            JavaFXUtils.createExceptionDialog(e, stage);
        }

    }

}
