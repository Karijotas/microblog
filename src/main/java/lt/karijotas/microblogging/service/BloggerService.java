package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.configuration.PasswordEncoderConfig;
import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloggerService extends GenericService {
    private final BloggerRepository bloggerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public BloggerService(BloggerRepository bloggerRepository, PasswordEncoder passwordEncoder) {
        this.bloggerRepository = bloggerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Blogger create(Blogger blogger) {
        var newUser = new Blogger();
        newUser.setUserName(blogger.getUserName());
        String encodedPassword = passwordEncoder.encode(blogger.getPassword());
        newUser.setPassword(encodedPassword);
        return bloggerRepository.save(newUser);
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

    public List<Blogger> getAllNotCurrentUsers(String username) {
        Long id = findByUserName(username).getId();
        return bloggerRepository.findAll()
                .stream()
                .filter(blogger -> !blogger.getId().equals(id))
                .toList();
    }
}
