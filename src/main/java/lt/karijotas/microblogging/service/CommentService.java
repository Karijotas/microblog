package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;
import lt.karijotas.microblogging.model.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static lt.karijotas.microblogging.model.mapper.CommentMapper.toComment;

@Service
public class CommentService extends GenericService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Boolean validateLength(Comment comment) {
        return !comment.getContent().isEmpty();
    }

    public Comment create(CommentEntityDto commentEntityDto) {
        Post post = postRepository.getById(commentEntityDto.getPostId());
        if (validateLength(toComment(commentEntityDto))) {
            var newComment = new Comment();
            newComment.setId(commentEntityDto.getId());
            newComment.setContent(commentEntityDto.getContent());
            newComment.setPost(post);
            return commentRepository.save(newComment);
        }
        throw new BlogValidationExeption("Comment shouldn't be empty");
    }

    @Override
    public List<CommentEntityDto> getAll() {
        return commentRepository.findAll()
                .stream()
                .map(CommentMapper::toCommentEntityDto)
                .toList();
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Comment> getById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
