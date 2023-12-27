package lt.karijotas.microblogging.dao;

import lt.karijotas.microblogging.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
