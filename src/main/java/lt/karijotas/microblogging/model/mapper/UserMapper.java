package lt.karijotas.microblogging.model.mapper;

import lt.karijotas.microblogging.model.User;
import lt.karijotas.microblogging.model.dto.UserEntityDto;

public class UserMapper {

    public static UserEntityDto toUserEntityDto(User user) {
        var userEntityDto = new UserEntityDto();
        userEntityDto.setId(user.getId());
        userEntityDto.setName(user.getUserName());
        userEntityDto.setPassword(user.getPassword());
        return userEntityDto;
    }

    public static User toUser(UserEntityDto entityDto){
        var user = new User();
        user.setId(entityDto.getId());
        user.setUserName(entityDto.getName());
        user.setPassword(entityDto.getPassword());
        user.setPostList(entityDto.getPostList());
        return user;
    }
}
