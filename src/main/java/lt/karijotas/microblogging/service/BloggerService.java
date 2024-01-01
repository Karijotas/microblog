package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.Blogger;

import java.util.List;
import java.util.Optional;

public interface BloggerService {
    Blogger create(Blogger blogger);

    Blogger update(Blogger blogger, Long id);

    Blogger findByUserName(String username);

    List<Blogger> getAll();

    Optional<Blogger> getById(Long id);

    Boolean deleteById(Long id);

    List<Blogger> getAllNotCurrentUsers(String username);
}
