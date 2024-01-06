package lt.karijotas.microblogging.model.dto;

import java.util.Objects;
import java.util.UUID;

public class CommentEntityDto extends CommentDto {

    public UUID id;

    public CommentEntityDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
