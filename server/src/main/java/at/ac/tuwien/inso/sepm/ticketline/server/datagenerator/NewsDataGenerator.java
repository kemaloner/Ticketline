package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsImageRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.util.FileUtil;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDataGenerator.class);
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 30;
    private static final String PATH = "/datagenerator/newsImage/";
    private static final String NEWS_IMAGE = "newsImage%d.jpg";
    private static final int NUMBER_OF_NEWS_IMAGE_TO_GENERATE = 20;
    //private static List <String>IMAGE_URL = new ArrayList();

    private final NewsRepository newsRepository;
    private final NewsImageRepository newsImageRepository;
    private final Faker faker;

    public NewsDataGenerator(NewsRepository newsRepository, NewsImageRepository newsImageRepository) {
        this.newsRepository = newsRepository;
        this.newsImageRepository = newsImageRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateNews() {
        if (newsRepository.count() > 0) {
            LOGGER.info("news already generated");
            return;
        }
        LOGGER.info("generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);

        String[] imageUrl = {"newsimage.jpg", "newsimage2.jpg", "newsimage3.jpg", "newsimage4.jpg", "newsimage5.jpg"};
        for(String url : imageUrl){
            try {
                copyImageFile(url);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage());
            }
        }

        for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
            List<NewsImage> newsImages = new ArrayList<>();
            for (int j = (int)(Math.random()*imageUrl.length); j < imageUrl.length; j+=1+(int)(Math.random()*imageUrl.length)) {
                NewsImage newsImage = NewsImage.builder().imageUrl(imageUrl[j]).build();
                LOGGER.debug("saving news image {}", newsImage);
                newsImageRepository.save(newsImage);
                newsImages.add(newsImage);
            }
            String title = faker.lorem().sentence(3,5);
            News news = News.builder()
                .title(title)
                .text(faker.lorem().paragraph(faker.number().numberBetween(5, 10)))
                .images(newsImages)
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            LOGGER.debug("saving news {}", news);
            newsRepository.save(news);
        }
    }

    private void copyImageFile(String imageUrl) throws UnsupportedEncodingException {
        File source = new File(URLDecoder.decode(getClass().getResource(String.format(PATH + imageUrl)).getPath(), "UTF-8"));
        File dest = FileUtil.file(imageUrl);
        try {
            FileUtil.copy(source, dest);
        } catch (IOException e) {
            LOGGER.error("failed image copy", e);
        }
    }

}
