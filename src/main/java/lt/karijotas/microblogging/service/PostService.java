package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {
    Boolean validateLength(Post post);

    Post create(PostEntityDto post);

    Post update(Post post, Long id);

    String[] splitPost(Post post);

    Integer wordCount(Post post);

    Map<String, Long> mostUsedWords(Post post, Long limit);

    List<Post> getAll();

    List<Post> getAllByCurrentAuthor(Long id);

    List<Post> getAllByAuthor(Long id);

    void increaseViewCount(Post post);

    Boolean validateOwnership(Long userId, Long postId);

    Boolean deleteById(Long id);

    Optional<Post> getById(Long id);
}
