package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.NewsImageDTO;
    import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
    import org.mapstruct.Mapper;

    import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsImageMapper {

    NewsImage imageDTOToImage(NewsImageDTO imageDTO);

    NewsImageDTO imageToImageDTO(NewsImage image);

    List<NewsImageDTO> imageToImageDTO(List<NewsImage> image);
}
