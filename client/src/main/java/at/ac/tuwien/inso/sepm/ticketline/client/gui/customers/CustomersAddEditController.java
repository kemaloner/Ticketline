package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomersAddEditController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomersAddEditController.class);

    @FXML
    private Label lblMandatoryFields;

    @FXML
    public TextField tfPhoneNumber;

    @FXML
    public TextField tfName;

    @FXML
    public TextField tfSurname;

    @FXML
    public TextField tfEmail;

    @FXML
    public DatePicker dpBirthdate;

    @FXML
    public TextArea taAdress;

    @FXML
    public Button btnBack;

    @FXML
    public Button btnSave;

    @FXML
    public Label lbFirstname;

    @FXML
    public Label lbSurname;

    @FXML
    public Label lb_birhtdate;

    @FXML
    public Label lbAddress;

    @FXML
    public Label lbPhonenumber;

    @FXML
    public Label lbEmail;

    private Runnable goToBack;

    private LocalizationSubject localizationSubject;

    private Alert alertInfo;
    private Alert alertError;
    private CustomerDTO customerDTO;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final CustomerService customerService;

    public CustomersAddEditController(MainController mainController, CustomerService customerService, LocalizationSubject localizationSubject) {
        this.mainController = mainController;
        this.localizationSubject = localizationSubject;
        this.customerService = customerService;
    }

    @FXML
    public void OnClickedBack() {

        LOGGER.info("Back to previous page from CustomersAddEditController");
        goBack();

    }

    private void goBack (){

        if(goToBack!=null){
            goToBack.run();
            clear();
        }
    }

    @FXML
    public void OnClickedSave() {

        LOGGER.info("Next button clicked");

        String fistname = tfName.getText();
        String surname = tfSurname.getText();
        String email = tfEmail.getText();
        String adress = taAdress.getText();
        String phoneNumber = tfPhoneNumber.getText();
        LocalDate birthday = dpBirthdate.getValue();

        CustomerDTO customerDTO;

        if (this.customerDTO == null) {
            customerDTO = CustomerDTO.builder()
                .firstname(fistname)
                .surname(surname)
                .email(email)
                .address(adress)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .build();
        }else{
            customerDTO = CustomerDTO.builder()
                .id(this.customerDTO.getId())
                .firstname(fistname)
                .surname(surname)
                .email(email)
                .address(adress)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .build();
        }

        alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertInfo.initModality(Modality.APPLICATION_MODAL);

        alertError = new Alert(Alert.AlertType.ERROR);
        alertError.initModality(Modality.APPLICATION_MODAL);


        try{
            customerService.validateCustomer(customerDTO);
        }catch (ClientServiceValidationException e){
            alertError.setTitle(BundleManager.getBundle().getString("dialog.customer.error.title"));
            alertError.setHeaderText(BundleManager.getBundle().getString("dialog.customer.error.header"));
            alertError.setContentText(e.getMessage());
            alertError.show();
            return;
        }

        Task<Void> workerTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if(customerDTO.getId() == null){
                    customerService.saveCustomer(customerDTO);
                }else{
                    customerService.updateCustomer(customerDTO);
                }

                return null;
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Customer succesfully created or updated");
                super.succeeded();
                alertInfo.setTitle(BundleManager.getBundle().getString("dialog.customer.success.title"));
                alertInfo.setHeaderText(BundleManager.getBundle().getString("dialog.customer.success.header"));
                alertInfo.setContentText(BundleManager.getBundle().getString("dialog.customer.success.content"));
                alertInfo.show();
                goBack();
            }

            @Override
            protected void failed() {
                LOGGER.error("Error while creating new Customer");
                super.failed();
                alertError.setTitle(BundleManager.getBundle().getString("dialog.customer.error.title"));
                alertError.setHeaderText(BundleManager.getBundle().getString("dialog.customer.error.header"));
                alertError.setContentText(BundleManager.getBundle().getString("dialog.customer.error.content_1"));
                alertError.show();

            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 50)
        );
        new Thread(workerTask).start();


    }


    public void initialize() {

        LOGGER.info("Initialize CustomersAddEditController");

        if(customerDTO == null){
            tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
            tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.addNew"));
        }else{
            tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
            tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.details"));
            tfName.setText(customerDTO.getFirstname());
            tfSurname.setText(customerDTO.getSurname());
            tfEmail.setText(customerDTO.getEmail());
            tfPhoneNumber.setText(customerDTO.getPhoneNumber());
            dpBirthdate.setValue(customerDTO.getBirthday());
            taAdress.setText(customerDTO.getAddress());
        }

        localizationSubject.attach(this);

    }

    private void clear(){
        localizationSubject.detach(this);
        customerDTO = null;
    }


    public void load(CustomerDTO customerDTO, Runnable goToBack) {
        clear();
        this.customerDTO = customerDTO;
        this.goToBack = goToBack;
        initialize();
    }

    @Override
    public void update() {

        LOGGER.info("Update CustomersAddEditController");

        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.addNew"));

        btnBack.setText(BundleManager.getBundle().getString("button.back"));
        btnSave.setText(BundleManager.getBundle().getString("button.save"));

        lbFirstname.setText(BundleManager.getBundle().getString("customer.name.*"));
        lbSurname.setText(BundleManager.getBundle().getString("customer.surname.*"));
        lb_birhtdate.setText(BundleManager.getBundle().getString("customer.birthdate.*"));
        lbAddress.setText(BundleManager.getBundle().getString("customer.adress"));
        lbPhonenumber.setText(BundleManager.getBundle().getString("customer.phonenumer"));
        lbEmail.setText(BundleManager.getBundle().getString("customer.email.*"));
        lblMandatoryFields.setText(BundleManager.getBundle().getString("customer.fields"));

    }

    public void setTabHeaderController(TabHeaderController tabHeaderController){
        this.tabHeaderController = tabHeaderController;
    }
}
