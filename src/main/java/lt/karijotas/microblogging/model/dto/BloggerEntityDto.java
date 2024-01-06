package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class BloggerEntityDto extends BloggerDto {
    private UUID id;

    public BloggerEntityDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
