package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsImageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)

public class NewsImageMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class ImageMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private NewsImageMapper imageMapper;

    private static final long IMAGE_ID = 1L;
    private static final String IMAGE_URL = "image-url.jpg";

    @Test
    public void shouldMapImageToSimpleImageDTO() {
        NewsImage image = NewsImage.builder()
            .imageUrl(IMAGE_URL)
            .id(IMAGE_ID)
            //.news(new News())
            .build();

        NewsImageDTO imageDTO = imageMapper.imageToImageDTO(image);

        assertThat(imageDTO).isNotNull();
        assertThat(imageDTO.getId()).isEqualTo(1L);
        assertThat(imageDTO.getImageUrl()).isEqualTo(IMAGE_URL);
    }

    @Test
    public void shouldMapSimpleImageDTOToImage() {

        NewsImageDTO imageDTO = NewsImageDTO.builder()
            .id(IMAGE_ID)
            .imageUrl(IMAGE_URL)
            .build();

        NewsImage image = imageMapper.imageDTOToImage(imageDTO);

        assertThat(image).isNotNull();
        assertThat(image.getId()).isEqualTo(1L);
        assertThat(image.getImageUrl()).isEqualTo(IMAGE_URL);
    }
}
