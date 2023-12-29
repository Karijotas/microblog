package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService extends GenericService {
    private final BloggerRepository bloggerRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(BloggerRepository bloggerRepository, PostRepository postRepository) {
        this.bloggerRepository = bloggerRepository;
        this.postRepository = postRepository;
    }

    public Post create(PostEntityDto post) {
        Blogger blogger = bloggerRepository.getById(post.getBloggerId());

        var newPost = new Post();
        newPost.setId(post.getId());
        newPost.setName(post.getName());
        newPost.setBody(post.getBody());
        newPost.setBlogger(blogger);
        return postRepository.save(newPost);
    }

    public Post update(Post post, Long id) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", id.toString()));
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

    @Override
    public Boolean deleteById(Long id) {
        try {
            bloggerRepository.deleteById(id);
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
