package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.BloggerRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceImplTest {

    @Mock
    private BloggerRepository bloggerRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_postSuccessfully() {
        PostEntityDto postEntityDto = new PostEntityDto();
        postEntityDto.setId(1L);
        postEntityDto.setName("Sample Post");
        postEntityDto.setBody("This is a sample post body");
        postEntityDto.setBloggerId(1L);

        Blogger blogger = new Blogger();
        blogger.setId(1L);
        blogger.setUserName("John Doe");

        Post createdPost = new Post();
        createdPost.setId(1L);
        createdPost.setName(postEntityDto.getName());
        createdPost.setBody(postEntityDto.getBody());
        createdPost.setBlogger(blogger);
        when(bloggerRepository.getById(1L)).thenReturn(blogger);
        when(postRepository.save(any(Post.class))).thenReturn(createdPost);
        Post savedPost = postServiceImpl.create(postEntityDto);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isEqualTo(1L);
        assertThat(savedPost.getName()).isEqualTo(postEntityDto.getName());
        assertThat(savedPost.getBody()).isEqualTo(postEntityDto.getBody());
        assertThat(savedPost.getBlogger()).isEqualTo(blogger);
    }

    @Test
    void update_existingPostSuccessfully() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setName("Existing Post");
        existingPost.setBody("This is an existing post body");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post updatedPost = new Post();
        updatedPost.setName("Updated Post");
        updatedPost.setBody("This is an updated post body");
        Post updated = postServiceImpl.update(updatedPost, 1L);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(existingPost.getId());
        assertThat(updated.getName()).isEqualTo(updatedPost.getName());
        assertThat(updated.getBody()).isEqualTo(updatedPost.getBody());
    }

    @Test
    void splitPost_validPost_ReturnsArrayOfWords() {
        Post post = new Post();
        post.setBody("This is a test post. It has some words.");
        String[] words = postServiceImpl.splitPostBody(post);
        assertThat(words).isNotNull();
        assertThat(words).containsExactly("this", "is", "a", "test", "post", "it", "has", "some", "words");
    }

    @Test
    void wordCount_validPost_ReturnsWordCount() {
        Post post = new Post();
        post.setBody("This is a test post. It has some words.");
        int count = postServiceImpl.wordCount(post);
        assertThat(count).isEqualTo(9);
    }

    @Test
    void mostUsedWords_validPostAndLimit_ReturnsMostUsedWords() {
        Post post = new Post();
        post.setBody("This is a test post. It has some words. This is a test post for testing.");
        when(postServiceImpl.splitPostBody(post)).thenReturn(new String[]{"this", "is", "a", "test", "post", "it", "has", "some", "words", "this", "is", "a", "test", "post", "for", "testing"});
        Map<String, Long> mostUsed = postServiceImpl.mostUsedWords(post, 3L);
        assertThat(mostUsed).isNotNull();
        assertThat(mostUsed.size()).isLessThanOrEqualTo(3);
        assertThat(mostUsed.keySet()).containsExactlyInAnyOrderElementsOf(Arrays.asList("test", "a", "post"));
    }

    @Test
    void getAllPosts_ReturnsAllPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        when(postRepository.findAll()).thenReturn(posts);
        List<Post> found = postServiceImpl.getAll();
        assertEquals(2, found.size());
    }

    @Test
    void deleteById_DeletesSuccessfully() {
        doNothing().when(postRepository).deleteById(anyLong());
        boolean deleted = postServiceImpl.deleteById(1L);
        assertTrue(deleted);
    }

    @Test
    void deleteById_FailsToDelete() {
        doThrow(EmptyResultDataAccessException.class).when(postRepository).deleteById(anyLong());
        boolean deleted = postServiceImpl.deleteById(1L);
        assertFalse(deleted);
    }

    @Test
    void findById_FindsSuccessfully() {
        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Post found = postServiceImpl.getById(1L).get();
        assertEquals(found, post);
    }

    @Test
    void getAllByAuthorId_FindsSuccessfully() {
        Blogger blogger = new Blogger();
        blogger.setId(1L);
        blogger.setUserName("John Doe");
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        when(postRepository.findAllByBloggerId(1L)).thenReturn(posts);
        PostServiceImpl postServiceImplSpy = spy(postServiceImpl);
        List<Post> retrievedPosts = postServiceImplSpy.getAllByAuthor(1L);
        verify(postServiceImplSpy, times(posts.size())).increaseViewCount(any(Post.class));
        assertEquals(posts, retrievedPosts);
    }
}