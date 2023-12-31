package lt.karijotas.microblogging.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostDto {
    private String name;
    private String body;
    private Long bloggerId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;

    public PostDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getBloggerId() {
        return bloggerId;
    }

    public void setBloggerId(Long bloggerId) {
        this.bloggerId = bloggerId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(getName(), postDto.getName()) && Objects.equals(getBody(), postDto.getBody()) && Objects.equals(getBloggerId(), postDto.getBloggerId()) && Objects.equals(getCreatedDate(), postDto.getCreatedDate()) && Objects.equals(getModifiedDate(), postDto.getModifiedDate()) && Objects.equals(getCreatedBy(), postDto.getCreatedBy()) && Objects.equals(getModifiedBy(), postDto.getModifiedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBody(), getBloggerId(), getCreatedDate(), getModifiedDate(), getCreatedBy(), getModifiedBy());
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", bloggerId=" + bloggerId +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
