package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.dao.UserRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService extends GenericService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post create(Post post) {
        var newPost = new Post();
        newPost.setId(post.getId());
        newPost.setName(post.getName());
        newPost.setBody(post.getBody());
        newPost.setCommentList(post.getCommentList());
        return postRepository.save(newPost);
    }

    public Post update(Post post, Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new BlogValidationExeption("Post doesn't exist", "id", "Post doesn't exist", id.toString()));
        existingPost.setName(post.getName());
        existingPost.setBody(post.getBody());
        existingPost.setCommentList(post.getCommentList());
        return postRepository.save(existingPost);
    }

    @Override
    public List getAll() {
        return super.getAll();
    }

    @Override
    public Boolean deleteById(Long id) {
        return super.deleteById(id);
    }

    @Override
    public Optional getById(Long id) {
        return super.getById(id);
    }
}
