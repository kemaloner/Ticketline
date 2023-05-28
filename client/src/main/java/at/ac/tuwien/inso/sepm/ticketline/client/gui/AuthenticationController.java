package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationController implements LocalizationObserver {

    @FXML
    private TextField txtUsername;
    @FXML
    private Label lblUsername;

    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblPassword;

    @FXML
    private Button btnAuthenticate;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final MainController mainController;

    @Autowired
    private LocalizationSubject localizationSubject;


    public AuthenticationController(
        AuthenticationService authenticationService,
        AuthenticationInformationService authenticationInformationService,
        UserService userService, MainController mainController
    ) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.mainController = mainController;

        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(authenticationTokenInfo));
    }

    @FXML
    private void initialize(){
        localizationSubject.attach(this);
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        Task<AuthenticationTokenInfo> task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    ((Node) actionEvent.getTarget()).getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }


    private void setAuthenticated(AuthenticationTokenInfo info) {
        //txtUsername.clear();
        if (info != null) {
            txtPassword.clear();
        }
    }

    @Override
    public void update() {
        lblUsername.setText(BundleManager.getBundle().getString("authenticate.username"));
        txtUsername.setPromptText(BundleManager.getBundle().getString("authenticate.username"));

        lblPassword.setText(BundleManager.getBundle().getString("authenticate.password"));
        txtPassword.setPromptText(BundleManager.getBundle().getString("authenticate.password"));

        btnAuthenticate.setText(BundleManager.getBundle().getString("authenticate.authenticate"));
    }
}