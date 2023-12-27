package lt.karijotas.microblogging.dao;

import lt.karijotas.microblogging.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

}
