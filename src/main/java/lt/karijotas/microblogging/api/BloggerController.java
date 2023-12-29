package lt.karijotas.microblogging.api;

import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;
import lt.karijotas.microblogging.service.BloggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static lt.karijotas.microblogging.model.mapper.BloggerMapper.toUser;
import static lt.karijotas.microblogging.model.mapper.BloggerMapper.toUserEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/blogger")
public class BloggerController {
    private final Logger logger = LoggerFactory.getLogger(BloggerController.class);
    private final BloggerService bloggerService;

    public BloggerController(BloggerService bloggerService) {
        this.bloggerService = bloggerService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,})
    @ResponseBody
    public List<Blogger> getAll() {
        return bloggerService.getAll();
    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<BloggerEntityDto> getUser(@PathVariable Long userId) {
        var userOptional = bloggerService.getById(userId);

        return userOptional
                .map(user -> ok(toUserEntityDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Blogger> register(@Valid @RequestBody Blogger blogger) {
        var createdUser = bloggerService.create(blogger);
        return ok(createdUser);
    }

    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.ok("No authenticated user found.");
        }
    }

    @GetMapping("/current-user/id")
    public ResponseEntity<Long> getCurrentUserId(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Long id = bloggerService.findByUserName(username).getId();
        return ResponseEntity.ok(id);
    }
}
