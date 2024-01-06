package lt.karijotas.microblogging.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "blogger")
public class Blogger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blogger_id")
    private UUID id;
    @NotBlank
    @Size(min = 1, max = 100, message = "A name shouldn't be longer than 100 characters or shorter than 1")
    private String userName;
    private String password;
    @OneToMany(mappedBy = "blogger")
    private List<Post> posts;

    public Blogger() {
    }

    public Blogger(UUID id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blogger blogger = (Blogger) o;
        return Objects.equals(getId(), blogger.getId()) && Objects.equals(getUserName(), blogger.getUserName()) && Objects.equals(getPassword(), blogger.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
