package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentEntityDto extends CommentDto {
    private Long id;

    public CommentEntityDto() {
    }

    public CommentEntityDto(Long id, String username, String comment, LocalDateTime createdDate, LocalDateTime modifiedDate, String createdBy, String modifiedBy) {
        super(username, comment, createdDate, modifiedDate, createdBy, modifiedBy);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getComment() {
        return super.getComment();
    }

    @Override
    public void setComment(String comment) {
        super.setComment(comment);
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
        CommentEntityDto that = (CommentEntityDto) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String
    toString() {
        return "CommentEntityDto{" +
                "id=" + id +
                '}';
    }
}
