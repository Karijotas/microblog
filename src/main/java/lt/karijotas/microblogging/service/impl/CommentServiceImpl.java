package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;
import lt.karijotas.microblogging.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static lt.karijotas.microblogging.model.mapper.CommentMapper.toComment;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Boolean validateLength(Comment comment) {
        return !comment.getContent().isEmpty();
    }

    @Override
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
    public Comment update(Comment entity, Long id) {
        throw new BlogValidationExeption("Method not implemented");
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
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

    @Override
    public List<Comment> getAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
