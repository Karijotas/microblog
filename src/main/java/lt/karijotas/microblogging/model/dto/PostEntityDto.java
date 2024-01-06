package lt.karijotas.microblogging.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class PostEntityDto extends PostDto {
    private UUID id;

    public PostEntityDto() {
        super();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
