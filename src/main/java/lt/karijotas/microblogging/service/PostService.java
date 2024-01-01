package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static lt.karijotas.microblogging.model.mapper.PostMapper.toPost;

@Service
public class PostService extends GenericService {
    private final BloggerRepository bloggerRepository;
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(PostService.class);


    @Autowired
    public PostService(BloggerRepository bloggerRepository, PostRepository postRepository) {
        this.bloggerRepository = bloggerRepository;
        this.postRepository = postRepository;
    }

    public Boolean validateLength(Post post) {
        return !post.getBody().isEmpty() || !post.getName().isEmpty();
    }

    public Post create(PostEntityDto post) {
        Blogger blogger = bloggerRepository.getById(post.getBloggerId());
        if (validateLength(toPost(post))) {
            var newPost = new Post();
            newPost.setId(post.getId());
            newPost.setName(post.getName());
            newPost.setBody(post.getBody());
            newPost.setBlogger(blogger);
            return postRepository.save(newPost);
        }
        throw new BlogValidationExeption("Post title and body shouldn't be empty");
    }

    public Post update(Post post, Long id) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", id.toString()));
        logger.info(existingPost.getName() + post.getName());
        existingPost.setName(post.getName());
        existingPost.setBody(post.getBody());
        return postRepository.save(existingPost);
    }

    public String[] splitPost(Post post) {
        String text = post.getBody();
        return text.replaceAll("[^\\p{L}\\p{Nd}]+", " ")
                .toLowerCase()
                .trim()
                .split("\\s+");
    }

    public Integer wordCount(Post post) {
        return splitPost(post).length;
    }

    public Map<String, Long> mostUsedWords(Post post, Long limit) {
        String[] words = splitPost(post);
        return Arrays.stream(words)
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    @Override
    public List<Post> getAll() {
        return postRepository.findAll();

    }

    public List<Post> getAllByCurrentAuthor(Long id) {
        return postRepository.findAllByBloggerId(id);
    }

    public List<Post> getAllByAuthor(Long id) {
        List<Post> posts = postRepository.findAllByBloggerId(id);
        posts.forEach(this::increaseViewCount);
        return posts;
    }

    public void increaseViewCount(Post post) {
        if (post.getCount() == null) {
            post.setCount(1);
        } else {
            post.setCount(post.getCount() + 1);
        }
        postRepository.save(post);
    }

    public Boolean validateOwnership(Long userId, Long postId) {
        return postRepository.findById(postId).stream().anyMatch(post -> post.getBlogger().getId().equals(userId));
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            postRepository.deleteById(id);
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
