package lt.karijotas.microblogging.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository<T,Long> extends JpaRepository<Object, Long> {

}
