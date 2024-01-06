package lt.karijotas.microblogging.dao;

import lt.karijotas.microblogging.model.Blogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BloggerRepository extends JpaRepository<Blogger, UUID> {
    Blogger findBloggerByUserName(String username);
}
