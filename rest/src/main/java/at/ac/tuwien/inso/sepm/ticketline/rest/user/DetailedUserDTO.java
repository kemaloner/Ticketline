package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;


@ApiModel(value = "DetailedUserDTO", description = "A simple DTO for user entries via rest")
public class DetailedUserDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The first name of the user")
    private String firstName;

    @ApiModelProperty(required = true, readOnly = true, name = "The last name of the user")
    private String lastName;

    @ApiModelProperty(required = true, readOnly = true, name = "The user name of the user")
    private String userName;

    @ApiModelProperty(required = true, readOnly = true, name = "The password of the user")
    private String password;

    @ApiModelProperty(required = true, readOnly = true, name = "The role of the user")
    private Integer role;

    @ApiModelProperty(required = true, readOnly = true, name = "The active status of the user")
    private boolean isActive;

    @ApiModelProperty(required = true, readOnly = true, name = "The time the user will be active again")
    private LocalDateTime activeTime;

    @ApiModelProperty(required = true, readOnly = true, name = "The login attempts of the user")
    private Integer attempts;

    @ApiModelProperty(required = true, readOnly = true, name = "The news read by the user")
    private List<SimpleNewsDTO> readNews;



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

    public List<SimpleNewsDTO> getReadNews() {
        return readNews;
    }

    public void setReadNews(List<SimpleNewsDTO> readNews) {
        this.readNews = readNews;
    }

    public LocalDateTime getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(LocalDateTime activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedUserDTO dto = (DetailedUserDTO) o;

        if (isActive != dto.isActive) return false;
        if (id != null ? !id.equals(dto.id) : dto.id != null) return false;
        if (firstName != null ? !firstName.equals(dto.firstName) : dto.firstName != null) return false;
        if (lastName != null ? !lastName.equals(dto.lastName) : dto.lastName != null) return false;
        if (userName != null ? !userName.equals(dto.userName) : dto.userName != null) return false;
        if (password != null ? !password.equals(dto.password) : dto.password != null) return false;
        if (role != null ? !role.equals(dto.role) : dto.role != null) return false;
        if (activeTime != null ? !activeTime.equals(dto.activeTime) : dto.activeTime != null) return false;
        if (attempts != null ? !attempts.equals(dto.attempts) : dto.attempts != null) return false;
        return readNews != null ? readNews.equals(dto.readNews) : dto.readNews == null;
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
        return "DetailedUserDTO{" +
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

    public static DetailedUserDTOBuilder builder(){
        return new DetailedUserDTOBuilder();
    }

    public static class DetailedUserDTOBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private Integer role;
        private boolean isActive;
        private LocalDateTime activeTime;
        private Integer attempts;
        private List<SimpleNewsDTO> readNews;

        public DetailedUserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DetailedUserDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public DetailedUserDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public DetailedUserDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public DetailedUserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DetailedUserDTOBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public DetailedUserDTOBuilder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DetailedUserDTOBuilder activeTime(LocalDateTime activeTime) {
            this.activeTime = activeTime;
            return this;
        }

        public DetailedUserDTOBuilder attempts(Integer attempts) {
            this.attempts = attempts;
            return this;
        }


        public DetailedUserDTOBuilder readNews(List<SimpleNewsDTO> readNews) {
            this.readNews = readNews;
            return this;
        }

        public DetailedUserDTO build() {
            DetailedUserDTO dto = new DetailedUserDTO();
            dto.setId(id);
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setUserName(userName);
            dto.setPassword(password);
            dto.setRole(role);
            dto.setActive(isActive);
            dto.setActiveTime(activeTime);
            dto.setAttempts(attempts);
            dto.setReadNews(readNews);
            return dto;
        }
    }


}
