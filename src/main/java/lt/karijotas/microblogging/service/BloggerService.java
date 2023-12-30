package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloggerService extends GenericService {
    private BloggerRepository bloggerRepository;

    @Autowired
    public BloggerService(BloggerRepository bloggerRepository) {
        this.bloggerRepository = bloggerRepository;
    }


    public Blogger create(Blogger blogger) {
        return bloggerRepository.save(blogger);
    }

    public Blogger update(Blogger blogger, Long id) {
        Blogger existingBlogger = bloggerRepository.findById(id)
                .orElseThrow(() -> new BlogValidationExeption("User doesn't exist", "id", "User doesn't exist", id.toString()));
        existingBlogger.setUserName(blogger.getUserName());
        existingBlogger.setPassword(blogger.getPassword());
        return bloggerRepository.save(existingBlogger);
    }

    public Blogger findByUserName(String username) {
        return bloggerRepository.findBloggerByUserName(username);
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
