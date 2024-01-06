package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;
import lt.karijotas.microblogging.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static lt.karijotas.microblogging.model.mapper.CommentMapper.toCommentEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping(value = "/post/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Comment> getAllByPostId(@PathVariable UUID postId) {
        return commentService.getAllByPostId(postId);
    }
    @GetMapping(value = "/count/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Integer getCommentCountByPostId(@PathVariable UUID postId) {
        return commentService.getAllByPostId(postId).size();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommentEntityDto> create(@Valid @RequestBody CommentEntityDto commentEntityDto) {
        var createdComment = commentService.create(commentEntityDto);
        return ok(toCommentEntityDto(createdComment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        logger.info("Attempt to delete Comment by id: {}", commentId);
        boolean deleted = commentService.deleteById(commentId);
        return (deleted ? ResponseEntity.noContent() : ResponseEntity.notFound()).build();
    }
}
