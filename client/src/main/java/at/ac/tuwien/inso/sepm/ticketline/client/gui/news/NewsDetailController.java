package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ClientServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsImageService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
public class NewsDetailController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDetailController.class);

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);


    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Pagination pagination;

    @FXML
    private ImageView imageView;

    private DetailedNewsDTO detailedNewsDTO;

    private NewsImageService newsImageService;

    public NewsDetailController(NewsImageService newsImageService){
        this.newsImageService = newsImageService;
    }

    private void initializeData(DetailedNewsDTO detailedNewsDTO) {
        lblDate.setText(NEWS_DTF.format(detailedNewsDTO.getPublishedAt()));
        lblTitle.setText(detailedNewsDTO.getTitle());
        lblText.setText(detailedNewsDTO.getText());


        if(detailedNewsDTO.getImages() != null && detailedNewsDTO.getImages().size() != 0){
            setImageView();
            paginationListener();
            pagination.setPageCount(detailedNewsDTO.getImages().size());
            pagination.setMaxPageIndicatorCount(Math.min(5, detailedNewsDTO.getImages().size()));
        }else{
            pagination.setDisable(true);
        }
    }

    private void paginationListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
            setImageView());
    }

    private void setImageView(){
        LOGGER.info("News image is set.");
        Task<NewsImageDTO> task = new Task<>() {
            @Override
            protected NewsImageDTO call() throws DataAccessException, ClientServiceValidationException {
                LOGGER.info("Loading detailed news.");
                NewsImageDTO newsImageDTO = detailedNewsDTO.getImages().get(pagination.getCurrentPageIndex());
                if(newsImageDTO.getByteArray() == null) {
                    newsImageDTO.setByteArray(newsImageService.findByOneId(newsImageDTO.getId()).getByteArray());
                }
                return newsImageDTO;
            }
            @Override
            protected void succeeded() {
                LOGGER.info("Loading detailed news is success.");
                imageView.setImage(new Image(new ByteArrayInputStream(getValue().getByteArray())));
                imageView.setPreserveRatio(false);
                imageView.setFitHeight(200);
                imageView.setFitWidth(270);
            }
            @Override
            protected void failed() {
                LOGGER.info("Loading detailed news is failed.");
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(), lblDate.getScene().getWindow()).showAndWait();
            }
        };
        new Thread(task).start();
    }

    void setDetailedNewsDTO(DetailedNewsDTO detailedNewsDTO){
        LOGGER.info("Detailed view is open.");
        this.detailedNewsDTO = detailedNewsDTO;
        initializeData(detailedNewsDTO);
    }
}
