package lt.karijotas.microblogging.service.impl;
import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.service.impl.BloggerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lt.karijotas.microblogging.model.mapper.BloggerMapper.toUserEntityDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BloggerServiceImplTest {

    @Mock
    private BloggerRepository bloggerRepository;
    @InjectMocks
    private lt.karijotas.microblogging.service.impl.BloggerServiceImpl BloggerServiceImpl;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_bloggerSuccessfully() {
        Blogger blogger = new Blogger();
        blogger.setId(1L);
        blogger.setUserName("John Doe");
        blogger.setPassword("password");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        BloggerServiceImpl BloggerServiceImpl = new BloggerServiceImpl(bloggerRepository, passwordEncoder);

        when(bloggerRepository.save(any(Blogger.class))).thenReturn(blogger);

        Blogger savedBlogger = BloggerServiceImpl.create(toUserEntityDto((blogger)));

        assertThat(savedBlogger).isNotNull();
        assertThat(savedBlogger.getId()).isEqualTo(1L);
        assertThat(savedBlogger.getUserName()).isEqualTo("John Doe");
        assertThat(savedBlogger.getPassword()).isEqualTo("password");
    }

    @Test
    void update_existingBloggerSuccessfully() {
        Blogger existingBlogger = new Blogger();
        existingBlogger.setId(1L);
        existingBlogger.setUserName("Existing User");
        existingBlogger.setPassword("existingpassword");

        when(bloggerRepository.findById(1L)).thenReturn(Optional.of(existingBlogger));
        when(bloggerRepository.save(any(Blogger.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Blogger updatedBlogger = new Blogger();
        updatedBlogger.setUserName("Updated User");
        updatedBlogger.setPassword("updatedpassword");

        Blogger updated = BloggerServiceImpl.update(updatedBlogger, 1L);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(existingBlogger.getId());
        assertThat(updated.getUserName()).isEqualTo(updatedBlogger.getUserName());
        assertThat(updated.getPassword()).isEqualTo(updatedBlogger.getPassword());
    }

    @Test
    void findByUserName_existingUserName_ReturnsBlogger() {
        Blogger existingBlogger = new Blogger();
        existingBlogger.setId(1L);
        existingBlogger.setUserName("JohnDoe");
        existingBlogger.setPassword("password");

        when(bloggerRepository.findBloggerByUserName("JohnDoe")).thenReturn(existingBlogger);
        Blogger foundBlogger = BloggerServiceImpl.findByUserName("JohnDoe");
        assertThat(foundBlogger).isNotNull();
        assertThat(foundBlogger.getId()).isEqualTo(1L);
        assertThat(foundBlogger.getUserName()).isEqualTo("JohnDoe");
        assertThat(foundBlogger.getPassword()).isEqualTo("password");
    }


    @Test
    void getAllBloggers_ReturnsAll() {
        List<Blogger> bloggers = new ArrayList<>();
        bloggers.add(new Blogger());
        bloggers.add(new Blogger());
        when(bloggerRepository.findAll()).thenReturn(bloggers);
        List<Blogger> found = BloggerServiceImpl.getAll();
        assertEquals(2, found.size());
    }

    @Test
    void deleteById_DeletesSuccessfully() {
        doNothing().when(bloggerRepository).deleteById(anyLong());
        boolean deleted = BloggerServiceImpl.deleteById(1L);
        assertTrue(deleted);
    }
    @Test
    void deleteBloggerById_FailsToDeleteBlogger() {
        doThrow(EmptyResultDataAccessException.class).when(bloggerRepository).deleteById(anyLong());
        boolean deleted = BloggerServiceImpl.deleteById(1L);
        assertFalse(deleted);
    }
    @Test
    void findBloggerById_FindsBloggerSuccessfully(){
        Blogger blogger = new Blogger();
        blogger.setId(1L);
        when(bloggerRepository.findById(blogger.getId())).thenReturn(Optional.of(blogger));
        Blogger found = BloggerServiceImpl.getById(1L).get();
        assertEquals(found, blogger);
    }
}
