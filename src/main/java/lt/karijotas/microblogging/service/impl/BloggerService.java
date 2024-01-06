package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BloggerService {
    private final BloggerRepository bloggerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public BloggerService(BloggerRepository bloggerRepository, PasswordEncoder passwordEncoder) {
        this.bloggerRepository = bloggerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    public Blogger create(BloggerEntityDto blogger) {
        var newUser = new Blogger();
        newUser.setUserName(blogger.getName());
        String encodedPassword = passwordEncoder.encode(blogger.getPassword());
        newUser.setPassword(encodedPassword);
        return bloggerRepository.save(newUser);
    }

    
    public Blogger update(Blogger blogger, UUID id) {
        Blogger existingBlogger = bloggerRepository.findById(id)
                .orElseThrow(() -> new BlogValidationExeption("User doesn't exist", "id", "User doesn't exist", id.toString()));
        existingBlogger.setUserName(blogger.getUserName());
        existingBlogger.setPassword(blogger.getPassword());
        return bloggerRepository.save(existingBlogger);
    }

    
    public Blogger findByUserName(String username) {
        return bloggerRepository.findBloggerByUserName(username);
    }

    
    public List<Blogger> getAll() {
        return bloggerRepository.findAll();
    }

    
    public Optional<Blogger> getById(UUID id) {
        return bloggerRepository.findById(id);
    }

    
    public Boolean deleteById(UUID id) {
        try {
            bloggerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public List<Blogger> getAllNotCurrentUsers(String username) {
        UUID id = findByUserName(username).getId();
        return bloggerRepository.findAll()
                .stream()
                .filter(blogger -> !blogger.getId().equals(id))
                .toList();
    }
}
