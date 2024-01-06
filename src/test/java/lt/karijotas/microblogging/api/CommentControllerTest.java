package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.service.impl.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentControllerTest {
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    public CommentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_ReturnsAllComments() {
        List<Comment> mockComments = Collections.singletonList(new Comment());
        when(commentService.getAll()).thenReturn(mockComments);
        List<Comment> comments = commentController.getAll();
        assertEquals(mockComments, comments);
    }

//    @Test
//    void getAllByPostId_ReturnsCommentsForPostId() {
//        Long postId = 1L;
//        List<Comment> mockComments = Collections.singletonList(new Comment());
//        when(commentServiceImpl.getAllByPostId(postId)).thenReturn(mockComments);
//        List<Comment> comments = commentController.getAllByPostId(postId);
//        assertEquals(mockComments, comments);
//    }
//
//    @Test
//    void deleteComment_ValidId_ReturnsNoContent() {
//        Long commentId = 1L;
//        when(commentServiceImpl.deleteById(commentId)).thenReturn(true);
//        ResponseEntity<Void> responseEntity = commentController.deleteComment(commentId);
//        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
//        verify(commentServiceImpl, times(1)).deleteById(anyLong());
//    }
//
//    @Test
//    void  getCommentCountByPostId_ReturnsCommentCount(){
//        Long postId = 1L;
//        Comment commentOne = new Comment();
//        Comment commentTwo = new Comment();
//        List<Comment> comments = List.of(commentOne, commentTwo);
//        when(commentServiceImpl.getAllByPostId(postId)).thenReturn(comments);
//        Integer commentCount = commentController.getCommentCountByPostId(postId);
//
//        assertEquals(commentCount, comments.size());
//    }
}
