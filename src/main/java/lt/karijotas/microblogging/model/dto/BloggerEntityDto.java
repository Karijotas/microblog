package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class BloggerEntityDto extends BloggerDto {
    private Long id;

    public BloggerEntityDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
