package lt.karijotas.microblogging.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @NotBlank
    @Size(min = 1, max = 100, message = "A name shouldn't be longer than 100 characters or shorter than 1")
    private String name;
    private String body;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blogger_id", referencedColumnName = "blogger_id", nullable = false)
    @NotNull
    private Blogger blogger;

    private Integer count;

    public Post() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getName(), post.getName()) && Objects.equals(getBody(), post.getBody()) && Objects.equals(getBlogger(), post.getBlogger()) && Objects.equals(getCount(), post.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getBody(), getBlogger(), getCount());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", blogger=" + blogger +
                ", count=" + count +
                '}';
    }
}
