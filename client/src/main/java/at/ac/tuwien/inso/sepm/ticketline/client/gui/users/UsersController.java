package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PaginationWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager.text;

@Component
public class UsersController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @FXML
    private TabHeaderController tabHeaderController;

    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnUserActive;
    @FXML
    private Button btnUserLocked;
    @FXML
    private Button btnRefresh;
    @FXML
    private TableView<SimpleUserDTO> tableUsers;
    @FXML
    private TableColumn<SimpleUserDTO,String> tcFirstName;
    @FXML
    private TableColumn<SimpleUserDTO,String> tcLastName;
    @FXML
    private TableColumn<SimpleUserDTO,String> tcUserName;
    @FXML
    private TableColumn<SimpleUserDTO,String> tcRole;
    @FXML
    private TableColumn<SimpleUserDTO,String> tcEnable;
    @FXML
    private Pagination pagination;

    private static final Integer NUMBER_OF_USER_PRO_TABLE = 16;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    private MainController mainController;

    private UserService userService;

    private LocalizationSubject localizationSubject;

    private Sort sort = Sort.by(Sort.Direction.ASC, "firstName");

    private Task<PaginationWrapper<SimpleUserDTO>> task1;

    public UsersController(MainController mainController, UserService userService,
                           LocalizationSubject localizationSubject){
        this.mainController = mainController;
        this.userService = userService;
        this.localizationSubject = localizationSubject;
    }

    @FXML
    private void initialize() {
        LOGGER.info("User Controller initialized");
        localizationSubject.attach(this);
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        tabHeaderController.setTitle(text("users.header_title"));
        btnSearch.setGraphic(fontAwesome.create("SEARCH"));
        btnRefresh.setGraphic(fontAwesome.create("REFRESH"));
        tableUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableUsers.getStylesheets().addAll(getClass().getResource("/css/tableview.css").toExternalForm());
        Map<TableColumn, String> columnNamesUser = new HashMap<>(){{
            put(tcFirstName, "firstName");
            put(tcLastName, "lastName");
            put(tcUserName, "lastName");
            put(tcUserName, "userName");
            put(tcRole, "role");
            put(tcEnable, "isActive");
        }};

        tableUsers.setOnSort(e -> {
            if(tableUsers.getSortOrder().isEmpty()) { return; }
            TableColumn tc = (TableColumn) tableUsers.getSortOrder().get(0);
            if (columnNamesUser.containsKey(tc)) {
                sort = Sort.by(tc.getSortType().equals(TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC, columnNamesUser.get(tc));
                setUpPagination();
            }
        });
        columnNamesUser.forEach((c, s) -> c.setCellValueFactory(new PropertyValueFactory<>(s)));
        tcRole.setCellValueFactory(v ->  new SimpleStringProperty(v.getValue().getRole()==1?"Admin":"User"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        tcEnable.setCellValueFactory(v -> {
            String str;
            if(!v.getValue().isActive()){
                str = text("users.locked");
            }
            else if(v.getValue().getActiveTime().isAfter(LocalDateTime.now())){
                str = text("users.locked") + " " + v.getValue().getActiveTime().format(formatter);
            }
            else {
                str = text("users.active");
            }
            return new SimpleStringProperty(str);
        });

        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                SimpleUserDTO user = tableUsers.getSelectionModel().getSelectedItem();
                boolean isActive = user.isActive() && user.getActiveTime().isBefore(LocalDateTime.now());
                btnUserActive.setDisable(isActive);
                btnUserLocked.setDisable(!isActive);
            }
            else{
                btnUserActive.setDisable(true);
                btnUserLocked.setDisable(true);
            }
        });
    }

    @FXML
    private void onKeyReleasedSearchField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            onClickedSearch();
        }
    }

    @FXML
    private void onClickedSearch() {
        LOGGER.info("Search button clicked");
        setUpPagination();
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(text("users.header_title"));
        tfSearch.setPromptText(text("users.search_field"));
        tcFirstName.setText(text("users.firstname"));
        tcLastName.setText(text("users.lastname"));
        tcUserName.setText(text("users.username"));
        tcRole.setText(text("users.role"));
        tcEnable.setText(text("users.enable"));
        btnUserActive.setText(text("button.active"));
        btnUserLocked.setText(text("button.locked"));
        tableUsers.refresh();
    }

    private void setActive(SimpleUserDTO user, boolean active){
        Task<SimpleUserDTO> task = new Task<>() {
            @Override
            protected SimpleUserDTO call() throws DataAccessException {
                return userService.setActive(user.getUserName(), active);
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                user.setActive(getValue().isActive());
                user.setActiveTime(getValue().getActiveTime());
                btnUserActive.setDisable(!btnUserActive.isDisabled());
                btnUserLocked.setDisable(!btnUserActive.isDisabled());
                tableUsers.refresh();
            }
            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),tableUsers.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private TableView loadUsersTable(Integer pageIndex){
        String keyword = tfSearch.getText();
        if (task1!=null && task1.isRunning()){ task1.cancel(); }
        task1 = new Task<>() {
            @Override
            protected PaginationWrapper<SimpleUserDTO> call() throws DataAccessException {
                tableUsers.getItems().clear();
                PageRequest request = PageRequest.of(pageIndex, NUMBER_OF_USER_PRO_TABLE, sort);
                return keyword.isEmpty() ? userService.findAll(request) : userService.findByKeyword(keyword, request);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                tableUsers.getItems().addAll(getValue().getEntities());
                pagination.setPageCount(getValue().getTotalPages());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), tableUsers.getScene().getWindow()).showAndWait();
            }
        };
        task1.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task1).start();
        return tableUsers;
    }

    public void onTabOpen(){
        tfSearch.clear();
        setUpPagination();
    }

    public void onTabClose(){

    }

    private void setUpPagination() {
        pagination.setPageFactory(this::loadUsersTable);
    }

    @FXML
    private void onClickedUserActive() {
        LOGGER.info("Activate user button clicked");
        SimpleUserDTO user = tableUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            setActive(user, true);
        }
    }

    @FXML
    private void onClickedUserLocked() {
        LOGGER.info("Lock user button clicked");
        SimpleUserDTO user = tableUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            setActive(user, false);
        }
    }

    @FXML
    public void refresh(){
        tfSearch.setText("");
        setUpPagination();
    }
}
