package lt.karijotas.microblogging.service.impl;

import lt.karijotas.microblogging.dao.CommentRepository;
import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;
import lt.karijotas.microblogging.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void create_commentSuccessfully() {
        CommentEntityDto commentEntityDto = new CommentEntityDto();
        commentEntityDto.setId(1L);
        commentEntityDto.setContent("Sample Content");
        commentEntityDto.setPostId(1L);

        Post post = new Post();
        post.setId(1L);
        post.setName("Title");
        post.setBody("Sample post body");
        post.setCount(2);
        post.setBlogger(new Blogger());

        Comment comment = new Comment();
        comment.setId(commentEntityDto.getId());
        comment.setContent(commentEntityDto.getContent());
        comment.setPost(post);

        when(postRepository.getById(1L)).thenReturn(post);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment saved = commentServiceImpl.create(commentEntityDto);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(commentEntityDto.getId());
        assertThat(saved.getContent()).isEqualTo(commentEntityDto.getContent());
        assertThat(saved.getPost()).isEqualTo(post);
    }

    @Test
    void getAllComments_ReturnsAllComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, "Comment 1", new Post()));
        comments.add(new Comment(2L, "Comment 2", new Post()));
        when(commentRepository.findAll()).thenReturn(comments);
        List<CommentEntityDto> found = commentServiceImpl.getAll();
        assertEquals(2, found.size());
    }

    @Test
    void deleteCommentById_DeletesCommentSuccessfully() {
        doNothing().when(commentRepository).deleteById(anyLong());
        boolean deleted = commentServiceImpl.deleteById(1L);
        assertTrue(deleted);
    }
    @Test
    void deleteCommentById_FailsToDeleteComment() {
        doThrow(EmptyResultDataAccessException.class).when(commentRepository).deleteById(anyLong());
        boolean deleted = commentServiceImpl.deleteById(1L);
        assertFalse(deleted);
    }
    @Test
    void findCommentById_FindsCommentSuccessfully(){
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Comment found = commentServiceImpl.getById(1L).get();
        assertEquals(found, comment);
    }
}
