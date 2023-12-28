package lt.karijotas.microblogging.dao;

import lt.karijotas.microblogging.model.Blogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloggerRepository extends JpaRepository<Blogger, Long> {
    Blogger findByUserName(String username);
}
