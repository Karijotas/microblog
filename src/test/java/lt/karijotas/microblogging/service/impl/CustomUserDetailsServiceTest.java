package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Mock
    private BloggerRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_existingUser_ReturnsUserDetails() {
        Blogger existingBlogger = new Blogger();
        existingBlogger.setId(1L);
        existingBlogger.setUserName("JohnDoe");
        existingBlogger.setPassword("password");
        when(userRepository.findBloggerByUserName("JohnDoe")).thenReturn(existingBlogger);
        when(passwordEncoder.encode(existingBlogger.getPassword())).thenReturn("password");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("JohnDoe");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("JohnDoe");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority())
                .isEqualTo("ROLE_USER");
    }

    @Test
    void loadUserByUsername_nonExistingUser_ThrowsException() {
        when(userRepository.findBloggerByUserName("NonExistingUser")).thenReturn(null);

        Throwable exception = catchThrowable(() -> customUserDetailsService.loadUserByUsername("NonExistingUser"));

        assertThat(exception)
                .isInstanceOf(BlogValidationExeption.class)
                .hasMessage("User not found");
    }


}