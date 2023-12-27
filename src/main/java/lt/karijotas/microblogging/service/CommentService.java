package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.dao.UserRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService extends GenericService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Comment create(Comment comment) {
        var newComment = new Comment();
        newComment.setId(comment.getId());
        newComment.setUsername(comment.getUsername());
        newComment.setComment(comment.getComment());
        return commentRepository.save(newComment);
    }

    public Comment update(Comment comment, Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new BlogValidationExeption("Comment doesn't exist", "id", "Comment doesn't exist", id.toString()));
        existingComment.setUsername(comment.getUsername());
        existingComment.setComment(comment.getComment());
        return commentRepository.save(existingComment);
    }

    @Override
    public List getAll() {
        return super.getAll();
    }

    @Override
    public Optional getById(Long id) {
        return super.getById(id);
    }

    @Override
    public Boolean deleteById(Long id) {
        return super.deleteById(id);
    }
}
