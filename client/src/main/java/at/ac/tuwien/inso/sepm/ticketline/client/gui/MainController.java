package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomersController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.EventsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.statistic.StatisticController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets.TicketsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.users.UsersController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.*;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Locale;

@Component
public class MainController implements LocalizationObserver {

    private static final int TAB_ICON_FONT_SIZE = 20;

    @FXML
    private VBox vBoxMain;
    @FXML
    private StackPane spMainContent;

    @FXML
    private ProgressBar pbLoadingProgress;

    @FXML
    private TabPane tpContent;

    @FXML
    private MenuBar mbMain;
    @FXML
    private Menu menuApplication;
    @FXML
    private MenuItem menuApplicationExit;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuHelpAbout;
    @FXML
    private Menu menuLanguages;
    @FXML
    private MenuItem menuLanguagesToEnglish;
    @FXML
    private MenuItem menuLanguagesToGerman;


    @FXML
    private MenuItem menu_application_logout;

    private Node login;

    @Autowired
    private AuthenticationService authenticationService;

    private AuthenticationInformationService authenticationInformationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private HallPlanService hallPlanService;

    private final SpringFxmlLoader springFxmlLoader;
    private final FontAwesome fontAwesome;
    private NewsController newsController;
    private UsersController usersController;
    public CustomersController customersController;
    private StatisticController statisticController;
    private EventsController eventsController;
    private TicketsController ticketsController;

    public Tab newsTab,usersTab,customersTab,statisticTab,eventsTab,ticketsTab;

    @Autowired
    private LocalizationSubject localizationSubject;

    public MainController(
        SpringFxmlLoader springFxmlLoader,
        FontAwesome fontAwesome,
        AuthenticationInformationService authenticationInformationService
    ) {
        this.springFxmlLoader = springFxmlLoader;
        this.fontAwesome = fontAwesome;
        this.authenticationInformationService = authenticationInformationService;
        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(authenticationTokenInfo));
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> mbMain.setUseSystemMenuBar(true));
        pbLoadingProgress.setProgress(0);
        localizationSubject.attach(this);
        boolean loadHallPlan = false; // temporary
        if(loadHallPlan){
            spMainContent.getChildren().add(springFxmlLoader.load("/fxml/tickets/hallPlanComponent.fxml"));
        }
        else{
            login = springFxmlLoader.load("/fxml/authenticationComponent.fxml");
            spMainContent.getChildren().add(login);
            initAllTab();
        }
    }

    private void initAllTab() {
        initNewsTabPane();
        initUsersTabPane();
        initCustomersTabPane();
        initStatisticTabPane();
        initEventsTabPane();
        initTicketsTabPane();
        tpContent.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (oldTab != null) {
                if (oldTab.equals(newsTab)) {
                    newsController.onTabClose();
                } else if (oldTab.equals(usersTab)) {
                    usersController.onTabClose();
                } else if (oldTab.equals(customersTab)) {
                    customersController.onTabClose();
                } else if (oldTab.equals(statisticTab)) {
                    statisticController.onTabClose();
                } else if (oldTab.equals(eventsTab)) {
                    eventsController.onTabClose();
                } else if (oldTab.equals(ticketsTab)) {
                    ticketsController.onTabClose();
                }
            }

            if(newTab.equals(newsTab)){ newsController.onTabOpen(); }
            else if(newTab.equals(usersTab)){ usersController.onTabOpen(); }
            else if(newTab.equals(customersTab)){ customersController.onTabOpen(); }
            else if(newTab.equals(statisticTab)){ statisticController.onTabOpen(); }
            else if(newTab.equals(eventsTab)){ eventsController.onTabOpen(); }
            else if(newTab.equals(ticketsTab)){ ticketsController.onTabOpen(); }
        });
        spMainContent.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if(event.getCode() == KeyCode.F1){
                aboutApplication(null);
            }
            if(authenticationInformationService.getCurrentAuthenticationToken().isPresent()){
                switch (event.getCode()){
                    case F2:
                        tpContent.getSelectionModel().select(newsTab);
                        break;
                    case F3:
                        tpContent.getSelectionModel().select(customersTab);
                        break;
                    case F4:
                        tpContent.getSelectionModel().select(statisticTab);
                        break;
                    case F5:
                        tpContent.getSelectionModel().select(eventsTab);
                        break;
                    case F6:
                        tpContent.getSelectionModel().select(ticketsTab);
                        break;
                    case F7:
                        if(userService.isAdmin()){
                            tpContent.getSelectionModel().select(usersTab);
                        }
                        break;
                }
            }

        });
    }

    public void deleteTemporaryTicketIter(TicketDTO temporaryTicket){
        try{
            ticketService.delete(temporaryTicket);
        }catch (DataAccessException e){
            JavaFXUtils.createExceptionDialog(e,tpContent.getScene().getWindow()).showAndWait();
        }
    }

    @FXML
    private void exitApplication() {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        authenticationService.destroy();
    }

    @FXML
    private void logoutApplication() {
        authenticationService.deAuthenticate();
    }

    @FXML
    private void aboutApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene(springFxmlLoader.load("/fxml/aboutDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("dialog.about.title"));
        dialog.showAndWait();
    }

    private void initGraphTab(Tab tab, String glyphSymbol){
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setFontSize(TAB_ICON_FONT_SIZE);
        glyph.setColor(Color.WHITE);
        tab.setGraphic(glyph);

    }

    private void initNewsTabPane() {
        SpringFxmlLoader.Wrapper<NewsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = wrapper.getController();
        newsTab = new Tab(null, wrapper.getLoadedObject());
        initGraphTab(newsTab,"NEWSPAPER_ALT");
    }

    private void initUsersTabPane() {
        SpringFxmlLoader.Wrapper<UsersController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/users/usersComponent.fxml");
        usersController = wrapper.getController();
        usersTab = new Tab(null, wrapper.getLoadedObject());
        initGraphTab(usersTab,"USER");
    }

    private void initCustomersTabPane() {
        SpringFxmlLoader.Wrapper<CustomersController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/customers/customersComponent.fxml");
        customersController = wrapper.getController();
        customersTab = new Tab(null, wrapper.getLoadedObject());
        customersController.setTab(customersTab);
        initGraphTab(customersTab,"USERS");
    }

    private void initStatisticTabPane() {
        SpringFxmlLoader.Wrapper<StatisticController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/statistic/top10Events.fxml");
        statisticController = wrapper.getController();
        statisticTab = new Tab(null, wrapper.getLoadedObject());
        initGraphTab(statisticTab,"BAR_CHART");
    }

    private void initEventsTabPane() {
        SpringFxmlLoader.Wrapper<EventsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/events/eventsComponent.fxml");
        eventsController = wrapper.getController();
        eventsTab = new Tab(null, wrapper.getLoadedObject());
        eventsController.setTab(eventsTab);
        initGraphTab(eventsTab,"CALENDAR");
    }

    private void initTicketsTabPane() {
        SpringFxmlLoader.Wrapper<TicketsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/tickets/ticketsComponent.fxml");
        ticketsController = wrapper.getController();
        ticketsTab = new Tab(null, wrapper.getLoadedObject());
        initGraphTab(ticketsTab,"TICKET");
    }

    private void setAuthenticated(AuthenticationTokenInfo info) {
        menu_application_logout.setDisable(info == null);
        if (info != null) {
            if (spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().remove(login);
                try {
                    userService.setCurrentUserByUserName(info.getUsername());
                } catch (DataAccessException e) {
                    JavaFXUtils.createExceptionDialog(e, tpContent.getScene().getWindow()).showAndWait();
                }
                tpContent.getTabs().clear();
                tpContent.getTabs().add(newsTab);
                tpContent.getTabs().add(customersTab);
                tpContent.getTabs().add(statisticTab);
                tpContent.getTabs().add(eventsTab);
                tpContent.getTabs().add(ticketsTab);
                if (userService.isAdmin()) {
                    tpContent.getTabs().add(usersTab);
                }
                selectTab(newsTab);
                newsController.onTabOpen();
            }
        } else {
            if (!spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().add(login);
            }
        }
    }

    public void selectTab(Tab tab){
        tpContent.getSelectionModel().select(tab);
    }

    public void setProgressbarProgress(double progress) {
        pbLoadingProgress.setProgress(progress);
    }

    @FXML
    public void setLocalToGerman(){
        BundleManager.changeLocale(Locale.GERMAN);
    }

    @FXML
    public void setLocalToEnglish(){
        BundleManager.changeLocale(Locale.ENGLISH);
    }

    @Override
    public void update() {
        menuApplication.setText(BundleManager.getBundle().getString("menu.application"));
        menuHelp.setText(BundleManager.getBundle().getString("menu.help"));
        menuLanguages.setText(BundleManager.getBundle().getString("menu.language"));
        menuApplicationExit.setText(BundleManager.getBundle().getString("menu.application.exit"));
        menuHelpAbout.setText(BundleManager.getBundle().getString("menu.help.about"));
        menuLanguagesToEnglish.setText(BundleManager.getBundle().getString("menu.language.english"));
        menuLanguagesToGerman.setText(BundleManager.getBundle().getString("menu.language.german"));
        menu_application_logout.setText(BundleManager.getBundle().getString("menu.application.logout"));
    }

    public VBox getvBoxMain(){
        return vBoxMain;
    }

}
