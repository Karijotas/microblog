package lt.karijotas.microblogging.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericService<T, D> {
    T create(D entity);
    T update(T entity, UUID id);
    List<T> getAll();
    Optional<T> getById(UUID id);
    Boolean deleteById(UUID id);
    Boolean validateLength(T entity);
}