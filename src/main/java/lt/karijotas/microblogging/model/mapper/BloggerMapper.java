package lt.karijotas.microblogging.model.mapper;

import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;

public class BloggerMapper {

    public static BloggerEntityDto toUserEntityDto(Blogger blogger) {
        var userEntityDto = new BloggerEntityDto();
        userEntityDto.setId(blogger.getId());
        userEntityDto.setName(blogger.getUserName());
        userEntityDto.setPassword(blogger.getPassword());
        return userEntityDto;
    }

    public static Blogger toUser(BloggerEntityDto entityDto) {
        var user = new Blogger();
        user.setUserName(entityDto.getName());
        user.setPassword(entityDto.getPassword());
        return user;
    }
}
