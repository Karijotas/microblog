package lt.karijotas.microblogging.model.dto;

import java.util.Objects;
import java.util.UUID;

public class CommentDto {
    private String content;
    private UUID postId;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(getContent(), that.getContent()) && Objects.equals(getPostId(), that.getPostId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent(), getPostId());
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "content='" + content + '\'' +
                ", postId=" + postId +
                '}';
    }
}
