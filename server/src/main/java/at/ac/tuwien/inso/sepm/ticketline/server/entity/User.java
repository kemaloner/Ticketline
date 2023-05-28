package at.ac.tuwien.inso.sepm.ticketline.server.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    public static final Integer ATTEMPTS = 5;
    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_USER = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long id;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false, name = "lastname")
    private String lastName;

    @Column(nullable = false, unique = true, name = "username")
    private String userName;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "role")
    private Integer role = ROLE_USER;

    @Column(nullable = false, name = "isactive")
    private boolean isActive = true;

    @Column(nullable = false, name = "activetime")
    private LocalDateTime activeTime;

    @Column(nullable = false, name = "attempts")
    private Integer attempts = ATTEMPTS;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "readNews",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "news_id"))
    private List<News> readNews = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public List<News> getReadNews() {
        return readNews;
    }

    public void setReadNews(List<News> readNews) {
        this.readNews = readNews;
    }

    public LocalDateTime getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(LocalDateTime activeTime) {
        this.activeTime = activeTime;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isActive != user.isActive) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (activeTime != null ? !activeTime.equals(user.activeTime) : user.activeTime != null) return false;
        if (attempts != null ? !attempts.equals(user.attempts) : user.attempts != null) return false;
        return readNews != null ? readNews.equals(user.readNews) : user.readNews == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (activeTime != null ? activeTime.hashCode() : 0);
        result = 31 * result + (attempts != null ? attempts.hashCode() : 0);
        result = 31 * result + (readNews != null ? readNews.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", isActive=" + isActive +
            ", activeTime=" + activeTime +
            ", attempts=" + attempts +
            ", readNews=" + readNews +
            '}';
    }

    public static class UserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private Integer role = ROLE_USER;
        private boolean isActive = true;
        private LocalDateTime activeTime;
        private Integer attempts = ATTEMPTS;
        private List<News> readNews = new ArrayList<>();

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public UserBuilder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public UserBuilder activeTime(LocalDateTime activeTime) {
            this.activeTime = activeTime;
            return this;
        }

        public UserBuilder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public UserBuilder readNews(List<News> readNews) {
            this.readNews = readNews;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            user.setPassword(password);
            user.setRole(role);
            user.setActive(isActive);
            user.setActiveTime(activeTime);
            user.setAttempts(attempts);
            user.setReadNews(readNews);
            return user;
        }
    }

}
