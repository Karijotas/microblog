package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostEntityDto extends PostDto {
    private Long id;

    public PostEntityDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
