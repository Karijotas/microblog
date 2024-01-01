package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Boolean validateLength(Comment comment);

    Comment create(CommentEntityDto commentEntityDto);

    List<CommentEntityDto> getAll();

    Boolean deleteById(Long id);

    Optional<Comment> getById(Long id);

    List<Comment> getAllByPostId(Long postId);
}


