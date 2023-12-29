package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;
import lt.karijotas.microblogging.service.BloggerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BloggerControllerTest {

    @Mock
    private BloggerService bloggerService;

    @InjectMocks
    private BloggerController bloggerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_ReturnsAllBloggers() {
        List<Blogger> mockBloggers = Collections.singletonList(new Blogger());

        when(bloggerService.getAll()).thenReturn(mockBloggers);

        List<Blogger> bloggers = bloggerController.getAll();

        assertEquals(mockBloggers, bloggers);
    }

    @Test
    void getUser_BloggerExists_ReturnsBloggerEntityDto() {
        Long userId = 1L;
        Blogger mockBlogger = new Blogger();
        mockBlogger.setId(userId);

        when(bloggerService.getById(userId)).thenReturn(Optional.of(mockBlogger));
        ResponseEntity<BloggerEntityDto> responseEntity = bloggerController.getUser(userId);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void getUser_BloggerDoesNotExist_ReturnsNotFound() {
        Long userId = 1L;

        when(bloggerService.getById(userId)).thenReturn(Optional.empty());
        ResponseEntity<BloggerEntityDto> responseEntity = bloggerController.getUser(userId);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }


    @Test
    void register_ValidBlogger_ReturnsCreatedBlogger() {
        Blogger blogger = new Blogger();

        when(bloggerService.create(any(Blogger.class))).thenReturn(blogger);

        ResponseEntity<Blogger> responseEntity = bloggerController.register(blogger);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(blogger, responseEntity.getBody());
    }

    @Test
    void getCurrentUser_AuthenticatedUser_ReturnsUsername() {
        String username = "testUser";
        UserDetails userDetails = User.withUsername(username).password("password").roles("USER").build();

        ResponseEntity<String> responseEntity = bloggerController.getCurrentUser(userDetails);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(username, responseEntity.getBody());
    }

    @Test
    void getCurrentUser_UnauthenticatedUser_ReturnsNoUserFoundMessage() {
        ResponseEntity<String> responseEntity = bloggerController.getCurrentUser(null);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("No authenticated user found.", responseEntity.getBody());
    }
}
