package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.BloggerService;
import lt.karijotas.microblogging.service.CommentService;
import lt.karijotas.microblogging.service.PostService;
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
    private final PostService postService;
    private final BloggerService bloggerService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    public PostController(PostService postService, BloggerService bloggerService, CommentService commentService,
                          CommentRepository commentRepository) {
        this.postService = postService;
        this.bloggerService = bloggerService;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Post>> getAllByCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Long id = bloggerService.findByUserName(username).getId();
            return ResponseEntity.ok(postService.getAllByCurrentAuthor(id));
        }
        return null;
    }

    @GetMapping(value = "/user/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Post> getAllByUserId(@PathVariable Long userId) {
        return postService.getAllByAuthor(userId);
    }

    @GetMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Post getPost(@PathVariable Long postId) {
        var postOptional = postService.getById(postId).orElseThrow(
                () -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        logger.info("Returning a single post");
        return postOptional;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PostEntityDto> create(@Valid @RequestBody PostEntityDto postEntityDto) {
        var createdPost = postService.create(postEntityDto);
        return ok(toPostEntityDto(createdPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Long id = bloggerService.findByUserName(userDetails.getUsername()).getId();
            if (postService.validateOwnership(id, postId)) {
                logger.info("Attempt to delete Post by id: {}", postId);
                List<Comment> comments = commentService.getAllByPostId(postId);
                logger.info("Deleting {} comments from the post", comments.size());
                commentRepository.deleteAll(comments);
                comments.clear();
                boolean deleted = postService.deleteById(postId);
                if (deleted) {
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @Valid @RequestBody PostDto postDto, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Long id = bloggerService.findByUserName(userDetails.getUsername()).getId();
            if (postService.validateOwnership(id, postId)) {
                var updated = postService.update(toPost(postDto), postId);
                logger.info(updated.getName());
                logger.info(String.valueOf(ok(toPostDto(updated))));
                return ok(toPostDto(updated));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{postId}/wordcount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getPostWordCount(@PathVariable Long postId) {
        Post post = postService.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        Integer wordCount = postService.wordCount(post);
        return ResponseEntity.ok().body(wordCount);
    }


    @GetMapping(value = "/{postId}/wordcount/{limit}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Long>> mostUsedWords(@PathVariable Long postId, @PathVariable Long limit) {
        Post post = postService.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        Map<String, Long> mostUsedWords = postService.mostUsedWords(post, limit);
        return ResponseEntity.ok().body(mostUsedWords);
    }
}
