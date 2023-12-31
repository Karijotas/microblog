package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
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

import static lt.karijotas.microblogging.model.mapper.CommentMapper.toCommentEntityDto;
import static lt.karijotas.microblogging.model.mapper.PostMapper.toPostEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final PostService postService;
    private final CommentService commentService;

    public CommentController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<CommentEntityDto> getAll() {
        return commentService.getAll();
    }

    @GetMapping(value = "/post/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Comment> getAllByUserId(@PathVariable Long postId) {
        return commentService.getAllByPostId(postId);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommentEntityDto> create(@Valid @RequestBody CommentEntityDto commentEntityDto) {
        var createdComment = commentService.create(commentEntityDto);
        return ok(toCommentEntityDto(createdComment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        logger.info("Attempt to delete Comment by id: {}", commentId);
        boolean deleted = commentService.deleteById(commentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
