package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private BloggerRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Blogger blogger = userRepository.findByUserName(username);
        if (blogger == null) {
            throw new BlogValidationExeption("User not found");
        }
        String encodedPassword = passwordEncoder.encode(blogger.getPassword());
        return User.builder()
                .username(blogger.getUserName())
                .password(encodedPassword)
                .roles("USER")
                .build();
    }
}