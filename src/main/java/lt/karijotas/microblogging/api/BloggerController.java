package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;
import lt.karijotas.microblogging.service.impl.BloggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static lt.karijotas.microblogging.model.mapper.BloggerMapper.toUserEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/blogger")
public class BloggerController {
    private final Logger logger = LoggerFactory.getLogger(BloggerController.class);
    private final BloggerServiceImpl BloggerServiceImpl;

    public BloggerController(BloggerServiceImpl BloggerServiceImpl) {
        this.BloggerServiceImpl = BloggerServiceImpl;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Blogger> getAll() {
        return BloggerServiceImpl.getAll();
    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BloggerEntityDto> getUser(@PathVariable Long userId) {
        var userOptional = BloggerServiceImpl.getById(userId);

        return userOptional
                .map(user -> ok(toUserEntityDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Blogger> register(@Valid @RequestBody Blogger blogger) {
        var createdUser = BloggerServiceImpl.create(blogger);
        return ok(createdUser);
    }

    @GetMapping(value = "/current-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return ResponseEntity.ok(username);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No authenticated user found.");
    }

    @GetMapping(value = "/current-user/id", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getCurrentUserId(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Long id = BloggerServiceImpl.findByUserName(username).getId();
            return ResponseEntity.ok(id.toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No authenticated user found.");
    }

    @GetMapping(value = "/others", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Blogger> getAllNotCurrentUsers(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return BloggerServiceImpl.getAllNotCurrentUsers(username);
        }
        return null;
    }
}
