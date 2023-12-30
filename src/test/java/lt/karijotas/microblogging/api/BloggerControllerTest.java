package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;
import lt.karijotas.microblogging.service.BloggerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@AutoConfigureTestDatabase
class BloggerControllerTest {

    @Mock
    private BloggerService bloggerService;

    @Mock
    private BloggerRepository bloggerRepository;
    @InjectMocks
    private BloggerController bloggerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bloggerController = new BloggerController(bloggerService);
        ReflectionTestUtils.setField(bloggerService, "bloggerRepository", bloggerRepository);
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

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("No authenticated user found.", responseEntity.getBody());
    }

    @Test
    void switchUser_MultipleUsers_LogInLogOut_ReturnsCorrectCredentials() {
        String firstUsername = "firstUser";
        String firstPassword = "password1";
        Blogger first = new Blogger(1L, firstUsername, firstPassword);
        bloggerRepository.save(first);
        UserDetails firstUserDetails = User.withUsername(firstUsername).password(firstPassword).roles("USER").build();

        ResponseEntity<String> firstLoginResponse = bloggerController.getCurrentUser(firstUserDetails);
        assertEquals(200, firstLoginResponse.getStatusCodeValue());
        assertEquals(firstUsername, firstLoginResponse.getBody());

        Blogger nullUsernameBlogger = new Blogger();
        nullUsernameBlogger.setId(1L);
        nullUsernameBlogger.setUserName(null);

        when(bloggerService.findByUserName(null)).thenReturn(nullUsernameBlogger);
        ResponseEntity<String> responseEntity = bloggerController.getCurrentUserId(null);
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("No authenticated user found.", responseEntity.getBody());

        String secondUsername = "secondUser";
        String secondPassword = "password2";
        Blogger second = new Blogger(2L, secondUsername, secondPassword);
        UserDetails secondUserDetails = User.withUsername(second.getUserName()).password(second.getPassword()).roles("USER").build();

        ResponseEntity<String> secondLoginResponse = bloggerController.getCurrentUser(secondUserDetails);
        assertEquals(200, secondLoginResponse.getStatusCodeValue());
        assertEquals(secondUsername, secondLoginResponse.getBody());

        when(bloggerService.findByUserName("secondUser")).thenReturn(second);
        ResponseEntity<String> secondUserIdResponse = bloggerController.getCurrentUserId(secondUserDetails);
        assertEquals(200, secondUserIdResponse.getStatusCodeValue());
        assertEquals(bloggerService.findByUserName(secondUsername).getId().toString(), secondUserIdResponse.getBody());

        ResponseEntity<String> logoutResponse = bloggerController.getCurrentUser(null);
        assertEquals(404, logoutResponse.getStatusCodeValue());
        assertEquals("No authenticated user found.", logoutResponse.getBody());

        when(bloggerService.findByUserName("firstUser")).thenReturn(first);
        ResponseEntity<String> firstUserIdResponse = bloggerController.getCurrentUserId(firstUserDetails);
        assertEquals(200, firstUserIdResponse.getStatusCodeValue());
        assertEquals(bloggerService.findByUserName(firstUsername).getId().toString(), firstUserIdResponse.getBody());
    }

    @Test
    void getCurrentUserId_UnauthenticatedUser_ReturnsNoUserFound() {
        ResponseEntity<String> responseEntity = bloggerController.getCurrentUserId(null);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("No authenticated user found.", responseEntity.getBody());
    }
}
