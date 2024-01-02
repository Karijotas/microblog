package lt.karijotas.microblogging.model.mapper;

import lt.karijotas.microblogging.exception.BlogValidationExeption;
import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
import lt.karijotas.microblogging.service.impl.BloggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostMapper {

    private final BloggerServiceImpl BloggerServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(PostMapper.class);


    public PostMapper(BloggerServiceImpl BloggerServiceImpl) {
        this.BloggerServiceImpl = BloggerServiceImpl;
    }

    public static PostEntityDto toPostEntityDto(Post post) {
        var postEntityDto = new PostEntityDto();
        postEntityDto.setId(postEntityDto.getId());
        postEntityDto.setName(postEntityDto.getName());
        postEntityDto.setBody(postEntityDto.getBody());
        postEntityDto.setBloggerId(post.getBlogger().getId());
        return postEntityDto;
    }

    public static Post toPost(PostDto entityDto) {
        logger.info(entityDto.getName() + "toPost");
        var post = new Post();
        post.setName(entityDto.getName());
        logger.info(post.getName() + "toPost");
        post.setBody(entityDto.getBody());
        return post;
    }

    public static PostDto toPostDto(Post post) {
        logger.info(post.getName() + "toPostDto");
        var postDto = new PostDto();
        logger.info(postDto.getName() + "toPostDto");
        postDto.setName(post.getName());
        postDto.setBody(post.getBody());
        return postDto;
    }
}
