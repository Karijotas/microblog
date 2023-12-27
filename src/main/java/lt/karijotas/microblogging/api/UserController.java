package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.User;
import lt.karijotas.microblogging.model.dto.UserEntityDto;
import lt.karijotas.microblogging.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static lt.karijotas.microblogging.model.mapper.UserMapper.toUser;
import static lt.karijotas.microblogging.model.mapper.UserMapper.toUserEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,})
    @ResponseBody
    public List<User> getGroups() {
        return userService.getAll();
    }
    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<UserEntityDto> getUser(@PathVariable Long userId) {
        var userOptional = userService.getById(userId);

        var responseEntity = userOptional
                .map(groups -> ok(toUserEntityDto(groups)))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }

    public ResponseEntity<UserEntityDto> create(@Valid @RequestBody UserEntityDto userEntityDto) {
                var createdUser = userService.create(toUser(userEntityDto));
                return ok(toUserEntityDto(createdUser));
    }

}
