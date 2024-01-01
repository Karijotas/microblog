package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.service.BloggerService;
import lt.karijotas.microblogging.service.CommentService;
import lt.karijotas.microblogging.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static lt.karijotas.microblogging.model.mapper.PostMapper.toPostEntityDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;
    @Mock
    private BloggerService bloggerService;
    @Mock
    private CommentService commentService;
    @Mock
    private Blogger blogger;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPostWordCount_PostExists_ReturnsWordCount() {
        Long postId = 1L;
        Post mockPost = new Post();
        mockPost.setId(postId);

        when(postService.getById(postId)).thenReturn(Optional.of(mockPost));
        when(postService.wordCount(any(Post.class))).thenReturn(10);

        ResponseEntity<Integer> responseEntity = postController.getPostWordCount(postId);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(10, responseEntity.getBody());
    }

    @Test
    void getPostWordCount_PostDoesNotExist_ThrowsException() {
        Long postId = 1L;

        when(postService.getById(postId)).thenReturn(Optional.empty());

        try {
            postController.getPostWordCount(postId);
        } catch (BlogValidationExeption exception) {
            assertEquals("Post doesn't exist", exception.getMessage());
            assertEquals("id", exception.getField());
            assertEquals("Post doesn't exist", exception.getError());
            assertEquals(postId.toString(), exception.getRejectedValue());
        }
    }

    @Test
    void mostUsedWords_PostExists_ReturnsMostUsedWords() {
        Long postId = 1L;
        Long limit = 5L;

        Post mockPost = new Post();
        mockPost.setId(postId);

        Map<String, Long> mockMostUsedWords = new HashMap<>();
        mockMostUsedWords.put("word1", 10L);
        mockMostUsedWords.put("word2", 8L);

        when(postService.getById(postId)).thenReturn(Optional.of(mockPost));
        when(postService.mostUsedWords(any(Post.class), eq(limit))).thenReturn(mockMostUsedWords);

        ResponseEntity<Map<String, Long>> responseEntity = postController.mostUsedWords(postId, limit);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockMostUsedWords, responseEntity.getBody());
    }

    @Test
    void getAll_ReturnsAllPosts() {
        List<Post> mockPosts = Collections.singletonList(new Post());

        when(postService.getAll()).thenReturn(mockPosts);

        List<Post> posts = postController.getAll();

        assertEquals(mockPosts, posts);
    }

    @Test
    void getPost_PostExists_ReturnsPostEntityDto() {
        Long postId = 1L;
        Post mockPost = new Post();
        mockPost.setId(postId);
        mockPost.setBlogger(blogger);
        when(postService.getById(postId)).thenReturn(Optional.of(mockPost));
    }

    @Test
    void testCreatePost() {
        Blogger blogger = new Blogger();
        blogger.setId(1L);
        blogger.setUserName("testUser");
        Post post = new Post();
        post.setId(1L);
        post.setName("Test Post");
        post.setBody("This is a test post content.");
        post.setBlogger(blogger);

        when(postService.create(toPostEntityDto(post))).thenReturn(post);
        Post createdPost = postService.create(toPostEntityDto(post));
        assertNotNull(createdPost);
    }


    @Test
    void deletePost_PostExists_ReturnsNoContent() {
        Long postId = 1L;
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        Blogger blogger = mock(Blogger.class);
        when(bloggerService.findByUserName(anyString())).thenReturn(blogger);
        when(postService.validateOwnership(anyLong(), anyLong())).thenReturn(true);
        when(postService.deleteById(postId)).thenReturn(true);
        Comment comment = mock(Comment.class);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(commentService.getAllByPostId(postId)).thenReturn(comments);
        ResponseEntity<Void> responseEntity = postController.deletePost(postId, userDetails);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(postService).deleteById(postId);
        verify(bloggerService).findByUserName("username");
    }


    @Test
    void updatePost_ValidPostDto_ReturnsUpdatedPostDto() {
        Long postId = 1L;
        PostDto postDto = new PostDto();
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");

        Post updatedPost = new Post();
        when(postService.update(any(Post.class), eq(postId))).thenReturn(updatedPost);
        Blogger mockedBlogger = mock(Blogger.class);
        when(mockedBlogger.getId()).thenReturn(1L);
        when(bloggerService.findByUserName(anyString())).thenReturn(mockedBlogger);
        when(postService.validateOwnership(anyLong(), anyLong())).thenReturn(true);
        when(postService.update(any(Post.class), anyLong())).thenReturn(updatedPost);

        ResponseEntity<PostDto> responseEntity = postController.updatePost(postId, postDto, userDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllByCurrentUser_UserAuthenticated_ReturnsPosts() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        Blogger blogger = new Blogger();
        blogger.setId(1L);
        when(bloggerService.findByUserName("testUser")).thenReturn(blogger);

        List<Post> mockPosts = Arrays.asList(new Post(), new Post());
        when(postService.getAllByCurrentAuthor(anyLong())).thenReturn(mockPosts);

        ResponseEntity<List<Post>> response = postController.getAllByCurrentUser(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPosts, response.getBody());
    }

    @Test
    void testGetPost_PostExists_ReturnsPost() {
        Long postId = 1L;
        Post mockPost = new Post();
        mockPost.setId(postId);
        when(postService.getById(postId)).thenReturn(Optional.of(mockPost));

        Post retrievedPost = postController.getPost(postId);

        assertEquals(mockPost, retrievedPost);
    }

    @Test
    void testGetPost_PostDoesNotExist_ThrowsException() {
        Long postId = 1L;
        when(postService.getById(postId)).thenReturn(Optional.empty());

        try {
            postController.getPost(postId);
        } catch (BlogValidationExeption exception) {
            assertEquals("Post doesn't exist", exception.getMessage());
            assertEquals("id", exception.getField());
            assertEquals("Post doesn't exist", exception.getError());
            assertEquals(postId.toString(), exception.getRejectedValue());
        }
    }

    @Test
    void testGetAllByUserId_ReturnsPostsByUserId() {
        Long userId = 1L;

        List<Post> mockPosts = Arrays.asList(new Post(), new Post());
        when(postService.getAllByAuthor(userId)).thenReturn(mockPosts);

        List<Post> posts = postController.getAllByUserId(userId);

        assertEquals(mockPosts, posts);
    }
}
