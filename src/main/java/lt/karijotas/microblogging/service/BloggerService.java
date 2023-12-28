package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloggerService extends GenericService{
    private final BloggerRepository bloggerRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public BloggerService(BloggerRepository bloggerRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.bloggerRepository = bloggerRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }


    public Blogger create(Blogger blogger) {
        var newUser = new Blogger();
        newUser.setId(blogger.getId());
        newUser.setUserName(blogger.getUserName());
        newUser.setPassword(blogger.getPassword());
        return bloggerRepository.save(newUser);
    }

    public Blogger update(Blogger blogger, Long id) {
        Blogger existingBlogger = bloggerRepository.findById(id)
                .orElseThrow(() -> new BlogValidationExeption("User doesn't exist", "id", "User doesn't exist", id.toString()));
        existingBlogger.setUserName(blogger.getUserName());
        existingBlogger.setPassword(blogger.getPassword());
        return bloggerRepository.save(existingBlogger);
    }
    @Override
    public List<Blogger> getAll() {
        return bloggerRepository.findAll();
    }
    @Override
    public Optional<Blogger> getById(Long id) {
        return bloggerRepository.findById(id);
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            bloggerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
