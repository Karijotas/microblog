package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;

import java.util.List;

public interface CommentService extends GenericService<Comment, CommentEntityDto> {
    Boolean validateLength(Comment comment);

    List<Comment> getAllByPostId(Long postId);
}