package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static lt.karijotas.microblogging.model.mapper.PostMapper.*;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/post")
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,})
    @ResponseBody
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<PostEntityDto> getPost(@PathVariable Long postId) {
        var postOptional = postService.getById(postId);

        var responseEntity = postOptional
                .map(post -> ok(toPostEntityDto(post)))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }

    public ResponseEntity<PostEntityDto> create(@Valid @RequestBody PostEntityDto postEntityDto) {
        var createdPost = postService.create(toPost(postEntityDto));
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

    @GetMapping(value = "/{postId}/wordcount", produces = {MediaType.APPLICATION_JSON_VALUE,})
    public Integer getPostWordCount(@PathVariable Long postId) {
        Post post = postService.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        return postService.wordCount(post);
    }

    @GetMapping(value = "/{postId}/wordcount/{limit}", produces = {MediaType.APPLICATION_JSON_VALUE,})
    public Map<String, Integer> mostUsedWords(@PathVariable Long postId, @PathVariable Long limit) {
        Post post = postService.getById(postId)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", postId.toString()));
        return postService.mostUsedWords(post, limit);
    }

}
