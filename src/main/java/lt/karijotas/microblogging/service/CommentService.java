package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;

import java.util.List;

public interface CommentService extends GenericService<Comment, CommentEntityDto> {

    List<Comment> getAllByPostId(Long postId);
}