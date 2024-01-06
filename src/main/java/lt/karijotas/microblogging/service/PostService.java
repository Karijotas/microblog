package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PostService extends GenericService<Post, PostEntityDto> {

    String[] splitPostBody(Post post);

    Integer wordCount(Post post);

    Map<String, Long> mostUsedWords(Post post, Long limit);

    List<Post> getAllByCurrentAuthor(UUID id);

    List<Post> getAllByAuthor(UUID id);

    void increaseViewCount(Post post);

    Boolean validateOwnership(UUID userId, UUID postId);
}
