package lt.karijotas.microblogging.model.mapper;

import lt.karijotas.microblogging.model.Post;
import lt.karijotas.microblogging.model.dto.PostDto;
import lt.karijotas.microblogging.model.dto.PostEntityDto;
;

public class PostMapper {
    public static PostEntityDto toPostEntityDto(Post post) {
        var postEntityDto = new PostEntityDto();
        postEntityDto.setId(postEntityDto.getId());
        postEntityDto.setName(postEntityDto.getName());
        postEntityDto.setBody(postEntityDto.getBody());
        postEntityDto.setBloggerId(post.getBlogger().getId());
        return postEntityDto;
    }

    public static Post toPost(PostEntityDto entityDto) {
        var post = new Post();
        post.setId(entityDto.getId());
        post.setName(entityDto.getName());
        post.setBody(entityDto.getBody());
        return post;
    }

    public static Post toPost(PostDto entityDto) {
        var post = new Post();
        post.setName(entityDto.getName());
        post.setBody(entityDto.getBody());
        return post;
    }
    public static PostDto toPostDto(Post post){
        var postDto = new PostDto();
        postDto.setName(post.getName());
        postDto.setBody(post.getBody());
        return postDto;
    }
}
