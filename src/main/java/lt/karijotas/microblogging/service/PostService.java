package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;

import java.util.List;
import java.util.Map;

public interface PostService extends GenericService<Post, PostEntityDto> {

    String[] splitPost(Post post);

    Integer wordCount(Post post);

    Map<String, Long> mostUsedWords(Post post, Long limit);

    List<Post> getAllByCurrentAuthor(Long id);

    List<Post> getAllByAuthor(Long id);

    void increaseViewCount(Post post);

    Boolean validateOwnership(Long userId, Long postId);
}
