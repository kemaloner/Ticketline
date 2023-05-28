package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "SimpleUserDTO", description = "A simple DTO for user entries via rest")
public class SimpleUserDTO {

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

        SimpleUserDTO dto = (SimpleUserDTO) o;

        if (isActive != dto.isActive) return false;
        if (id != null ? !id.equals(dto.id) : dto.id != null) return false;
        if (firstName != null ? !firstName.equals(dto.firstName) : dto.firstName != null) return false;
        if (lastName != null ? !lastName.equals(dto.lastName) : dto.lastName != null) return false;
        if (userName != null ? !userName.equals(dto.userName) : dto.userName != null) return false;
        if (password != null ? !password.equals(dto.password) : dto.password != null) return false;
        if (role != null ? !role.equals(dto.role) : dto.role != null) return false;
        if (activeTime != null ? !activeTime.equals(dto.activeTime) : dto.activeTime != null) return false;
        return attempts != null ? attempts.equals(dto.attempts) : dto.attempts == null;
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
        return result;
    }

    @Override
    public String toString() {
        return "SimpleUserDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", isActive=" + isActive +
            ", activeTime=" + activeTime +
            ", attempts=" + attempts +
            '}';
    }

    public static SimpleUserDTOBuilder builder(){
        return new SimpleUserDTOBuilder();
    }

    public static class SimpleUserDTOBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private Integer role;
        private boolean isActive;
        private LocalDateTime activeTime;
        private Integer attempts;

        public SimpleUserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SimpleUserDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public SimpleUserDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public SimpleUserDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public SimpleUserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SimpleUserDTOBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public SimpleUserDTOBuilder active(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SimpleUserDTOBuilder activeTime(LocalDateTime activeTime) {
            this.activeTime = activeTime;
            return this;
        }

        public SimpleUserDTOBuilder attempts(Integer attempts) {
            this.attempts = attempts;
            return this;
        }

        public SimpleUserDTO build() {
            SimpleUserDTO dto = new SimpleUserDTO();
            dto.setId(id);
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setUserName(userName);
            dto.setPassword(password);
            dto.setRole(role);
            dto.setActive(isActive);
            dto.setActiveTime(activeTime);
            dto.setAttempts(attempts);
            return dto;
        }
    }


}
