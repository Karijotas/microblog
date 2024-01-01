package lt.karijotas.microblogging.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, D> {
    T create(D entity);
    T update(T entity, Long id);
    List<T> getAll();
    Optional<T> getById(Long id);
    Boolean deleteById(Long id);
}