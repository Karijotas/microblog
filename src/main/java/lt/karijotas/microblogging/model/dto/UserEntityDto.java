package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserEntityDto extends UserDto {
    private Long id;

    public UserEntityDto() {
    }

    public UserEntityDto(Long id, String name, String password, LocalDateTime createdDate, LocalDateTime modifiedDate, String createdBy, String modifiedBy) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        super.setCreatedDate(createdDate);
    }

    @Override
    public LocalDateTime getModifiedDate() {
        return super.getModifiedDate();
    }

    @Override
    public void setModifiedDate(LocalDateTime modifiedDate) {
        super.setModifiedDate(modifiedDate);
    }

    @Override
    public String getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    public void setCreatedBy(String createdBy) {
        super.setCreatedBy(createdBy);
    }

    @Override
    public String getModifiedBy() {
        return super.getModifiedBy();
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
        super.setModifiedBy(modifiedBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserEntityDto that = (UserEntityDto) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        return "UserEntityDto{" +
                "id=" + id +
                '}';
    }
}
