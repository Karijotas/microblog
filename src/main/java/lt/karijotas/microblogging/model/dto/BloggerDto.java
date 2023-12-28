package lt.karijotas.microblogging.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lt.karijotas.microblogging.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BloggerDto {
    private String name;
    private String password;
    private List<Post> postList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;

    public BloggerDto() {
    }

    public BloggerDto(String name, String password, List<Post> postList, LocalDateTime createdDate, LocalDateTime modifiedDate, String createdBy, String modifiedBy) {
        this.name = name;
        this.password = password;
        this.postList = postList;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
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
        BloggerDto bloggerDto = (BloggerDto) o;
        return Objects.equals(getName(), bloggerDto.getName()) && Objects.equals(getPassword(), bloggerDto.getPassword()) && Objects.equals(getCreatedDate(), bloggerDto.getCreatedDate()) && Objects.equals(getModifiedDate(), bloggerDto.getModifiedDate()) && Objects.equals(getCreatedBy(), bloggerDto.getCreatedBy()) && Objects.equals(getModifiedBy(), bloggerDto.getModifiedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword(), getCreatedDate(), getModifiedDate(), getCreatedBy(), getModifiedBy());
    }

    @Override
    public String toString() {
        return "BloggerDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
