package lt.karijotas.microblogging.model.dto;

import java.util.Objects;

public class CommentEntityDto extends CommentDto {

    public Long id;

    public CommentEntityDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
