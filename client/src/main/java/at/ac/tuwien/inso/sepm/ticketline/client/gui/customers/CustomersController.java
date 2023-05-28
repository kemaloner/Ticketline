package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.tickets.TicketDetailController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomersController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomersController.class);

    @FXML
    public TextField tfSearch;

    @FXML
    public Button btnSearch;

    @FXML
    public Button btnAddNew;

    @FXML
    public Button btnDetails;

    @FXML
    public Button btnNext;

    @FXML
    public Button btnBack;

    @FXML
    private Button btnRefresh;

    @FXML
    public TableView<CustomerDTO> tvCustomerTable;

    @FXML
    private TableColumn<CustomerDTO, String> tcName;

    @FXML
    private TableColumn<CustomerDTO, String> tcSurname;

    @FXML
    private TableColumn<CustomerDTO, LocalDate> tcBirthDate;

    @FXML
    private TableColumn<CustomerDTO, String> tcEmail;

    @FXML
    private TableColumn<CustomerDTO, String> tcPhoneNumber;

    @FXML
    public Pagination pagination;

    @FXML
    private TabHeaderController tabHeaderController;

    private Stage stage;

    private static final Integer NUMBER_OF_CUSTOMER_PRO_TABLE = 16;
    private Sort sort = Sort.by(Sort.Direction.ASC, "firstname");

    private Tab currentTab;
    private Task<PaginationWrapper<CustomerDTO>> customerTask;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private final TicketService ticketService;
    private TicketDTO ticketDTO;
    private Runnable goToBack;

    private LocalizationSubject localizationSubject;

    public CustomersController(CustomerService customerService, MainController mainController,
                               SpringFxmlLoader springFxmlLoader, TicketService ticketService,
                               LocalizationSubject localizationSubject) {

        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.localizationSubject = localizationSubject;
    }


    @FXML
    public void initialize() {

        LOGGER.info("Initalize CustomersController");

        localizationSubject.attach(this);
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        btnSearch.setGraphic(fontAwesome.create("SEARCH"));
        btnRefresh.setGraphic(fontAwesome.create("REFRESH"));
        tvCustomerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvCustomerTable.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());
        Map<TableColumn, String> columnNamesCustomer = new HashMap<>(){{
            put(tcName, "firstname");
            put(tcSurname, "surname");
            put(tcBirthDate, "birthday");
            put(tcPhoneNumber, "phoneNumber");
            put(tcEmail, "email");
        }};

        tvCustomerTable.setOnSort(e -> {
            if(tvCustomerTable.getSortOrder().isEmpty()) { return; }
            TableColumn tc =  tvCustomerTable.getSortOrder().get(0);
            if (columnNamesCustomer.containsKey(tc)) {
                sort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, columnNamesCustomer.get(tc));
                setUpPagination();
            }
        });
        columnNamesCustomer.forEach((c, s) -> c.setCellValueFactory(new PropertyValueFactory<>(s)));
    }

    @Override
    public void update() {

        LOGGER.info("Update CustomersController");

        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        tfSearch.setPromptText(BundleManager.getBundle().getString("customer.searchField"));
        btnAddNew.setText(BundleManager.getBundle().getString("button.addnew"));
        btnDetails.setText(BundleManager.getBundle().getString("button.details"));
        tcName.setText(BundleManager.getBundle().getString("customer.name"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.surname"));
        tcBirthDate.setText(BundleManager.getBundle().getString("customer.birthdate"));
        tcEmail.setText(BundleManager.getBundle().getString("customer.email"));
        tcPhoneNumber.setText(BundleManager.getBundle().getString("customer.phonenummer_"));
        tvCustomerTable.refresh();

    }

    public void load(TicketDTO ticketDTO, Runnable goToBack, Stage stage) {
        this.ticketDTO = ticketDTO;
        this.goToBack = goToBack;
        this.stage = stage;
        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(text("confirmation"));
            alert.setHeaderText(text("confirmation"));
            alert.setContentText(text("ticket.close.request") + "\n" + text("seats.will.be.freed"));
            Optional<ButtonType> selection = alert.showAndWait();
            if(selection.isPresent()){
                if(selection.get() == ButtonType.OK){
                    try{
                        ticketService.delete(ticketDTO);
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(text("info"));
                        alert.setTitle(text("info"));
                        alert.setContentText(text("seats.freed"));
                        alert.show();
                    }catch (DataAccessException a){
                        JavaFXUtils.createExceptionDialog(a, stage);
                    }
                }else{
                    e.consume();
                }
            }else{
                e.consume();
            }
        });
        setButtons();
    }

    private void setButtons(){
        btnNext.setVisible(this.ticketDTO != null);
        btnBack.setVisible(this.ticketDTO != null);
    }

    private TableView loadTable(Integer pageIndex){

        LOGGER.info("Load Customer Table for page: "+pageIndex);

        String keyword = tfSearch.getText();
        if (customerTask!=null && customerTask.isRunning()){ customerTask.cancel(); }
        customerTask = new Task<>() {
            @Override
            protected PaginationWrapper<CustomerDTO> call() throws DataAccessException {
                tvCustomerTable.getItems().clear();
                PageRequest request = PageRequest.of(pageIndex, NUMBER_OF_CUSTOMER_PRO_TABLE, sort);
                return keyword.isEmpty() ? customerService.findAll(request) : customerService.findByKeyword(keyword, request);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                tvCustomerTable.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), tvCustomerTable.getScene().getWindow()).showAndWait();
            }
        };
        customerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(customerTask).start();
        return tvCustomerTable;
    }

    public void setUpPagination() {
        pagination.setPageFactory(this::loadTable);
    }

    @FXML
    private void onKeyReleasedSearchField(KeyEvent keyEvent) {
        LOGGER.debug("On Clicked Seach Customer:" + keyEvent.getText());
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            onClickedSearch();
        }
    }

    @FXML
    private void onClickedSearch() {
        setUpPagination();
    }

    @FXML
    public void OnClickedAddNew() {

        LOGGER.debug("On Clicked Add New Customer");

        SpringFxmlLoader.Wrapper<CustomersAddEditController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/customers/customersAddEdit.fxml");
        if(stage != null){
            setUpCustomersAddEditController(null, wrapper);
        }else {
            Node node = mainController.customersTab.getContent();
            mainController.customersTab.setContent(wrapper.getLoadedObject());
            wrapper.getController().load(null, () -> {
                mainController.customersTab.setContent(node);
                setUpPagination();
            });
        }

    }

    @FXML
    public void OnClickedDetails() {

        LOGGER.debug("On Clicked Edit Customer");

        CustomerDTO selected = tvCustomerTable.getSelectionModel().getSelectedItem();

        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(text("info"));
            alert.setHeaderText(text("info"));
            alert.setContentText(text("customer.select"));

        }else{
            SpringFxmlLoader.Wrapper<CustomersAddEditController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/customers/customersAddEdit.fxml");
            if(stage != null){
                setUpCustomersAddEditController(selected, wrapper);
            }else{
                Node node = mainController.customersTab.getContent();
                mainController.customersTab.setContent(wrapper.getLoadedObject());
                wrapper.getController().load(selected, () -> {
                    mainController.customersTab.setContent(node);
                    setUpPagination();
                });
            }
        }

    }

    public void setTab(Tab tab){
        tfSearch.clear();
        this.currentTab = tab;
    }

    public void onTabOpen(){
        setUpPagination();
    }

    public void onTabClose(){
        btnNext.setVisible(false);
        btnBack.setVisible(false);
        ticketDTO = null;
    }

    @FXML
    public void OnClickedNext() {
        LOGGER.info("Next button clicked");
        Task<CustomerDTO> task = new Task<>() {
            @Override
            protected CustomerDTO call() throws DataAccessException {
                CustomerDTO customer = tvCustomerTable.getSelectionModel().getSelectedItem();
                if(customer == null){
                    customer = customerService.findOne(1L);
                }
                return customer;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                SpringFxmlLoader.Wrapper<TicketDetailController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/tickets/ticketsDetailComponent.fxml");
                ticketDTO.setCustomer(getValue());
                wrapper.getController().load(ticketDTO, goToHereInStage(), stage);
                stage.getScene().setRoot((Parent) wrapper.getLoadedObject());
                stage.getScene().getWindow().sizeToScene();
            }
            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),tvCustomerTable.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    @FXML
    public void OnClickedBack() {
        LOGGER.info("Back to previous page from CustomersController");
        if(goToBack!=null){
            goToBack.run();
            btnNext.setVisible(false);
            btnBack.setVisible(false);
            localizationSubject.detach(this);
        }
    }

    @FXML
    public void refresh(){
        LOGGER.info("Refresh button clicked.");
        tfSearch.setText("");
        setUpPagination();
    }

    private void setUpCustomersAddEditController(CustomerDTO selected, SpringFxmlLoader.Wrapper<CustomersAddEditController> wrapper){
        CustomersAddEditController controller = wrapper.getController();
        SpringFxmlLoader.Wrapper<TabHeaderController> tabHeaderControllerWrapper =
            springFxmlLoader.loadAndWrap("/fxml/tabHeader.fxml");
        TabHeaderController tabHeaderController = tabHeaderControllerWrapper.getController();
        controller.setTabHeaderController(tabHeaderController);

        controller.load(selected, goToHereInStage());

        stage.setScene(new Scene((Parent) wrapper.getLoadedObject()));
    }

    private Runnable goToHereInStage(){

        return () -> {
            SpringFxmlLoader.Wrapper<CustomersController> customersControllerWrapper =
                springFxmlLoader.loadAndWrap("/fxml/customers/customersComponent.fxml");
            CustomersController customersController = customersControllerWrapper.getController();
            customersController.setUpPagination();
            customersController.load(ticketDTO, goToBack, stage);
            stage.getScene().setRoot((Parent) customersControllerWrapper.getLoadedObject());
            stage.getScene().getWindow().sizeToScene();
        };
    }


}
