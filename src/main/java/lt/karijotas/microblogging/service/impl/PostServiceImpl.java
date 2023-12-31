package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static lt.karijotas.microblogging.model.mapper.PostMapper.toPost;

@Service
public class PostServiceImpl implements PostService {
    private final BloggerRepository bloggerRepository;
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(BloggerRepository bloggerRepository, PostRepository postRepository) {
        this.bloggerRepository = bloggerRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Boolean validateLength(Post post) {
        return !post.getBody().isEmpty() || !post.getName().isEmpty();
    }

    @Override
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

    @Override
    public Post update(Post post, Long id) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", id.toString()));
        logger.info(existingPost.getName() + post.getName());
        existingPost.setName(post.getName());
        existingPost.setBody(post.getBody());
        return postRepository.save(existingPost);
    }

    @Override
    public String[] splitPostBody(Post post) {
        String text = post.getBody();
        return text.replaceAll("[^\\p{L}\\p{Nd}]+", " ")
                .toLowerCase()
                .trim()
                .split("\\s+");
    }

    @Override
    public Integer wordCount(Post post) {
        return splitPostBody(post).length;
    }

    @Override
    public Map<String, Long> mostUsedWords(Post post, Long limit) {
        String[] words = splitPostBody(post);
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

    @Override
    public List<Post> getAllByCurrentAuthor(Long id) {
        return postRepository.findAllByBloggerId(id);
    }

    @Override
    public List<Post> getAllByAuthor(Long id) {
        List<Post> posts = postRepository.findAllByBloggerId(id);
        posts.forEach(this::increaseViewCount);
        return posts;
    }

    @Override
    public void increaseViewCount(Post post) {
        if (post.getCount() == null) {
            post.setCount(1);
        } else {
            post.setCount(post.getCount() + 1);
        }
        postRepository.save(post);
    }

    @Override
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
