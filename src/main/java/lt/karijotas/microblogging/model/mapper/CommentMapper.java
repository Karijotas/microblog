package lt.karijotas.microblogging.model.mapper;

import lt.karijotas.microblogging.dao.PostRepository;
import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Comment;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.CommentEntityDto;

public class CommentMapper {

    private static PostRepository postRepository;

    public CommentMapper(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public static Comment toComment(CommentEntityDto entityDto) {
        var newComment = new Comment();
        newComment.setId(entityDto.getId());
        newComment.setContent(entityDto.getContent());
        newComment.setPost(getPost(entityDto.getPostId()));
        return newComment;
    }

    public static CommentEntityDto toCommentEntityDto(Comment comment) {
        var newEntityDto = new CommentEntityDto();

        newEntityDto.setId(comment.getId());
        newEntityDto.setContent(comment.getContent());
        newEntityDto.setPostId(comment.getPost().getId());

        return newEntityDto;
    }

    private static Post getPost(Long postId) {
        try {
            return postRepository.getById(postId);
        } catch (Exception e) {
            throw new BlogValidationExeption(e.getMessage());
        }
    }
}
