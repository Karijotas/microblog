package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.BloggerService;
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
import java.util.ArrayList;
import java.util.Arrays;
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

    public PostController(PostService postService, BloggerService bloggerService) {
        this.postService = postService;
        this.bloggerService = bloggerService;
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
    public ResponseEntity<PostEntityDto> getPost(@PathVariable Long postId) {
        var postOptional = postService.getById(postId);

        return postOptional
                .map(post -> ok(toPostEntityDto(post)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PostEntityDto> create(@Valid @RequestBody PostEntityDto postEntityDto) {
        var createdPost = postService.create(postEntityDto);
        return ok(toPostEntityDto(createdPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        logger.info("Attempt to delete Post by id: {}", postId);
        boolean deleted = postService.deleteById(postId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @Valid @RequestBody PostDto postDto) {
        var updated = postService.update(toPost(postDto), postId);
        return ok(toPostDto(updated));
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
