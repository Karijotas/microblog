package lt.karijotas.microblogging.dao;

import lt.karijotas.microblogging.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends GenericRepository<Post, Long>
{
}
