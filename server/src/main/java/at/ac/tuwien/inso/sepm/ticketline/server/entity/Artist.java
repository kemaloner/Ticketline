package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_artist_id")
    @SequenceGenerator(name = "seq_artist_id", sequenceName = "seq_artist_id")
    private Long id;
    @Column(nullable = false, name = "firstname")
    private String firstname;
    @Column(nullable = false, name = "surname")
    private String surname;
    @ManyToMany()
    @JoinTable(name = "PerformanceArtist",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "performance_id"))
    private List<Performance> plays_in = new ArrayList<>();

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

    public List<Performance> getPlays_in() {
        return plays_in;
    }

    public void setPlays_in(List<Performance> plays_in) {
        this.plays_in = plays_in;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (id != null ? !id.equals(artist.id) : artist.id != null) return false;
        if (firstname != null ? !firstname.equals(artist.firstname) : artist.firstname != null) return false;
        if (surname != null ? !surname.equals(artist.surname) : artist.surname != null) return false;
        return plays_in != null ? plays_in.equals(artist.plays_in) : artist.plays_in == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (plays_in != null ? plays_in.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", surname='" + surname + '\'' +
            ", plays_in=" + plays_in +
            '}';
    }

    public static ArtistBuilder builder(){
        return new ArtistBuilder();
    }

    public static final class ArtistBuilder{

        private Long id;
        private String firstname;
        private String surname;
        private List<Performance> plays_in = new ArrayList<>();

        private ArtistBuilder(){
        }

        public ArtistBuilder id(Long id){
            this.id = id;
            return this;
        }

        public ArtistBuilder firstname(String firstname){
            this.firstname = firstname;
            return this;
        }

        public ArtistBuilder lastname(String surname){
            this.surname = surname;
            return this;
        }

        public ArtistBuilder performances(List<Performance> plays_in){
            this.plays_in = plays_in;
            return this;
        }

        public Artist build(){
            Artist artist = new Artist();
            artist.setId(id);
            artist.setFirstname(firstname);
            artist.setSurname(surname);
            artist.setPlays_in(plays_in);
            return artist;
        }
    }
}
