package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.impl.BloggerServiceImpl;
import lt.karijotas.microblogging.service.impl.CommentServiceImpl;
import lt.karijotas.microblogging.service.impl.PostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static lt.karijotas.microblogging.model.mapper.PostMapper.*;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/post")
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostServiceImpl postServiceImpl;
    private final BloggerServiceImpl BloggerServiceImpl;
    private final CommentServiceImpl commentServiceImpl;
    private final CommentRepository commentRepository;

    public PostController(PostServiceImpl postServiceImpl, BloggerServiceImpl BloggerServiceImpl, CommentServiceImpl commentServiceImpl,
                          CommentRepository commentRepository) {
        this.postServiceImpl = postServiceImpl;
        this.BloggerServiceImpl = BloggerServiceImpl;
        this.commentServiceImpl = commentServiceImpl;
        this.commentRepository = commentRepository;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Post> getAll() {
        return postServiceImpl.getAll();
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Post>> getAllByCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Long id = BloggerServiceImpl.findByUserName(username).getId();
            return ResponseEntity.ok(postServiceImpl.getAllByCurrentAuthor(id));
        }
        return null;
    }

    @GetMapping(value = "/user/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Post> getAllByUserId(@PathVariable Long userId) {
        return postServiceImpl.getAllByAuthor(userId);
    }

    @GetMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Post getPost(@PathVariable Long postId) {
        var postOptional = postServiceImpl.getById(postId).orElseThrow(
                () -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        return postOptional;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PostEntityDto> create(@Valid @RequestBody PostEntityDto postEntityDto) {
        var createdPost = postServiceImpl.create(postEntityDto);
        return ok(toPostEntityDto(createdPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Long id = BloggerServiceImpl.findByUserName(userDetails.getUsername()).getId();
            if (postServiceImpl.validateOwnership(id, postId)) {
                logger.info("Attempt to delete Post by id: {}", postId);
                List<Comment> comments = commentServiceImpl.getAllByPostId(postId);
                logger.info("Deleting {} comments from the post", comments.size());
                commentRepository.deleteAll(comments);
                comments.clear();
                boolean deleted = postServiceImpl.deleteById(postId);
                return (deleted ? ResponseEntity.noContent() : ResponseEntity.notFound()).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @Valid @RequestBody PostDto postDto, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Long id = BloggerServiceImpl.findByUserName(userDetails.getUsername()).getId();
            if (postServiceImpl.validateOwnership(id, postId)) {
                var updated = postServiceImpl.update(toPost(postDto), postId);
                return ok(toPostDto(updated));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{postId}/wordcount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getPostWordCount(@PathVariable Long postId) {
        Post post = postServiceImpl.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        Integer wordCount = postServiceImpl.wordCount(post);
        return ResponseEntity.ok().body(wordCount);
    }


    @GetMapping(value = "/{postId}/wordcount/{limit}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Long>> mostUsedWords(@PathVariable Long postId, @PathVariable Long limit) {
        Post post = postServiceImpl.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        Map<String, Long> mostUsedWords = postServiceImpl.mostUsedWords(post, limit);
        return ResponseEntity.ok().body(mostUsedWords);
    }
}
