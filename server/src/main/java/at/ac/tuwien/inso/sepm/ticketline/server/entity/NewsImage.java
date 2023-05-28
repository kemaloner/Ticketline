package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;

@Entity
@Table(name="news_image")
public class NewsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_image_id")
    @SequenceGenerator(name = "seq_image_id", sequenceName = "seq_image_id")
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Transient
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

    public static ImageBuilder builder() {
        return new ImageBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsImage image = (NewsImage) o;

        if (id != null ? !id.equals(image.id) : image.id != null) return false;
        return imageUrl != null ? image.equals(image.imageUrl) : image.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
    }

    public static final class ImageBuilder {
        private Long id;
        private String imageUrl;

        public ImageBuilder(){

        }

        public ImageBuilder id(Long id){
            this.id=id;
            return this;
        }

        public ImageBuilder imageUrl(String imageUrl){
            this.imageUrl=imageUrl;
            return this;
        }

        public NewsImage build(){
            NewsImage image = new NewsImage();
            image.setId(id);
            image.setImageUrl(imageUrl);
            return image;
        }
    }
}
