package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Blogger;
import lt.karijotas.microblogging.model.dto.BloggerEntityDto;

import java.util.List;

public interface BloggerService extends GenericService<Blogger, BloggerEntityDto> {
    Blogger findByUserName(String username);

    List<Blogger> getAllNotCurrentUsers(String username);
}