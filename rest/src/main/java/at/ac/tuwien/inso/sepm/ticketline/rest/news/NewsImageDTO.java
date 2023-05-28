package at.ac.tuwien.inso.sepm.ticketline.rest.news;

import io.swagger.annotations.ApiModelProperty;


public class NewsImageDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The Image-Url of the news")
    private String imageUrl;

    @ApiModelProperty(name = "The Image-Data of the news")
    private byte[] byteArray;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsImageDTO image = (NewsImageDTO) o;

        if (id != null ? !id.equals(image.id) : image.id != null) return false;
        return imageUrl != null ? image.equals(image.imageUrl) : image.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    public static ImageDTOBuilder builder() {
        return new ImageDTOBuilder();
    }

    public static final class ImageDTOBuilder {

        private Long id;
        private String imageUrl;

        public ImageDTOBuilder id(Long id){
            this.id=id;
            return this;
        }

        public ImageDTOBuilder imageUrl(String imageUrl){
            this.imageUrl=imageUrl;
            return this;
        }

        public NewsImageDTO build(){
            NewsImageDTO image = new NewsImageDTO();
            image.setId(id);
            image.setImageUrl(imageUrl);
            return image;
        }
    }
}
