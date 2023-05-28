package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsImageRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsImageService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SimpleNewsImageService implements NewsImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsImageService.class);

    private static final List<String> VALID_IMAGE_EXTENSIONS = Arrays.asList("jpg","jpeg","png");

    private final NewsImageRepository newsImageRepository;

    public SimpleNewsImageService(NewsImageRepository newsImageRepository){
        this.newsImageRepository = newsImageRepository;
    }

    @Override
    public NewsImage createNewsImage(NewsImage newsImage) throws IOException {
        if (newsImage.getId() == null){
            saveImageData(newsImage);
            return newsImageRepository.save(newsImage);
        } else {
            throw new ServerServiceValidationException("News image is not valid!");
        }
    }

    @Override
    public NewsImage findByOneId(Long id) {
        if (id != null) {
            NewsImage newsImage = newsImageRepository.findOneById(id).orElseThrow(NotFoundException::new);
            loadImageData(newsImage);
            return newsImage;
        } else {
            throw new ServerServiceValidationException("News image is not valid!");
        }
    }

    @Override
    public List<NewsImage> findAll() {
        return newsImageRepository.findAll();
    }

    private void loadImageData(NewsImage img) {
        LOGGER.info("Image data loading.");
        try {
            img.setByteArray(FileUtil.readAllBytes(img.getImageUrl()));
        } catch (IOException e) {
            throw new NotFoundException();
        }
    }

    private void saveImageData(NewsImage img) throws IOException {
        String fileName = img.getImageUrl();
        String extension = FileUtil.getExtension(fileName);
        if (!VALID_IMAGE_EXTENSIONS.contains(extension)){
            throw new IllegalArgumentException();
        }
        fileName = "newsimage" + System.currentTimeMillis() + "." + extension;
        FileUtil.write(fileName, img.getByteArray());
        img.setImageUrl(fileName);
    }
}
