package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsImageService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.util.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewsAddController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsAddController.class);
    public Button btnCancel;

    @FXML
    private TextArea tfText;
    @FXML
    private TextField tfTitle;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbText;
    @FXML
    private Button btnImageUpload;
    @FXML
    private Button btnSave;

    private NewsService newsService;

    private Runnable goToBack;

    private NewsController newsController;

    @FXML
    private TabHeaderController tabHeaderController;

    private LocalizationSubject localizationSubject;
    private NewsImageService newsImageService;


    private List<File> list;

    public NewsAddController(NewsService newsService, NewsImageService newsImageService, LocalizationSubject localizationSubject){
        this.newsService = newsService;
        this.newsImageService = newsImageService;
        this.localizationSubject = localizationSubject;
    }

    public void onImageUploadButtonClicked() {
        LOGGER.info("Image selection windows is open.");
        list = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.JPEG", "*.PNG", "*.JPG");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        fileChooser.setTitle("Open Image File");
        list = fileChooser.showOpenMultipleDialog(null);
    }


    public void onSaveButtonClicked() {
        LOGGER.info("Save button clicked.");
        Task<Stage> task = new Task<>() {
            @Override
            protected Stage call() throws DataAccessException, ClientServiceValidationException, IOException {
                LOGGER.info("Publishing news.");
                DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
                detailedNewsDTO.setText(tfText.getText());
                detailedNewsDTO.setTitle(tfTitle.getText());
                List<NewsImageDTO> imageList = new ArrayList<>();
                if(list != null){
                    for(File image : list){
                        NewsImageDTO newsImageDTO = new NewsImageDTO();
                        newsImageDTO.setImageUrl(image.getPath());
                        newsImageDTO.setByteArray(Files.readAllBytes(image.toPath()));
                        newsImageDTO = newsImageService.createImage(newsImageDTO);
                        imageList.add(newsImageDTO);
                    }
                }
                detailedNewsDTO.setImages(imageList);
                newsService.publishNews(detailedNewsDTO);
                return null;

            }
            @Override
            protected void succeeded() {
                LOGGER.info("Publishing news is success.");
                list = null;
                newsController.loadUnreadNews();
                if(goToBack!=null){
                    goToBack.run();
                }
            }
            @Override
            protected void failed() {
                LOGGER.info("Publishing news is failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), lbTitle.getScene().getWindow()).showAndWait();
            }
        };
        new Thread(task).start();
    }


    public void load(NewsController newsController, Runnable goToBack) {

        tfText.setWrapText(true);
        this.goToBack = goToBack;
        this.newsController = newsController;
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("news.add.stage.title"));
        localizationSubject.attach(this);

    }

    @Override
    public void update() {
        lbText.setText(BundleManager.getBundle().getString("news.add.text"));
        lbTitle.setText(BundleManager.getBundle().getString("news.add.title"));
        btnSave.setText(BundleManager.getBundle().getString("button.news.add.save"));
        btnCancel.setText(BundleManager.getBundle().getString("button.back"));
        btnImageUpload.setText(BundleManager.getBundle().getString("button.news.add.image"));
        tabHeaderController.setTitle(BundleManager.getBundle().getString("news.add.stage.title"));

    }

    public void onClickedCancel() {
        LOGGER.info("Cancel button clicked");
        if(goToBack!=null){
            goToBack.run();
            localizationSubject.detach(this);
        }
    }
}
