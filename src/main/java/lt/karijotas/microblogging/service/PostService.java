package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.dao.UserRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService extends GenericService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post create(Post post) {
        var newPost = new Post();
        newPost.setId(post.getId());
        newPost.setName(post.getName());
        newPost.setBody(post.getBody());
        newPost.setCommentList(post.getCommentList());
        return postRepository.save(newPost);
    }

    public Post update(Post post, Long id) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", id.toString()));
        existingPost.setName(post.getName());
        existingPost.setBody(post.getBody());
        existingPost.setCommentList(post.getCommentList());
        return postRepository.save(existingPost);
    }

    public String[] splitPost(Post post) {
        String text = post.getBody();
        return text.split("\\s+");
    }

    public Integer wordCount(Post post) {
        return splitPost(post).length;
    }

    public Map<String, Integer> mostUsedWords(Post post, Long limit) {
        String[] words = splitPost(post);
        Map<String, Integer> wordCount = Arrays.stream(words)
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.summingInt(word -> 1)
                ));
        return wordCount.entrySet()
                .stream()
                .limit(limit)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Boolean deleteById(Long id) {
         try {
            userRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }
}
