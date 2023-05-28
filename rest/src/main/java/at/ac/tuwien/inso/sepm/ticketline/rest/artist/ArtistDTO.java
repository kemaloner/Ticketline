package at.ac.tuwien.inso.sepm.ticketline.rest.artist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ArtistDTO", description = "A simple DTO for artist entries via rest")
public class ArtistDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The first name of the artist")
    private String firstname;

    @ApiModelProperty(required = true, readOnly = true, name = "The surname of the artist")
    private String surname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtistDTO artistDTO = (ArtistDTO) o;

        if (id != null ? !id.equals(artistDTO.id) : artistDTO.id != null) return false;
        if (firstname != null ? !firstname.equals(artistDTO.firstname) : artistDTO.firstname != null) return false;
        return surname != null ? surname.equals(artistDTO.surname) : artistDTO.surname == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", surname='" + surname + '\'' +
            '}';
    }

    public static ArtistDTOBuilder builder(){
        return new ArtistDTOBuilder();
    }

    public static final class ArtistDTOBuilder{

        private Long id;
        private String firstname;
        private String surname;

        public ArtistDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public ArtistDTOBuilder firstname(String  firstname){
            this.firstname = firstname;
            return this;
        }

        public ArtistDTOBuilder surname(String surname){
            this.surname = surname;
            return this;
        }

        public ArtistDTO builder(){
            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setId(id);
            artistDTO.setFirstname(firstname);
            artistDTO.setSurname(surname);
            return artistDTO;
        }
    }
}
