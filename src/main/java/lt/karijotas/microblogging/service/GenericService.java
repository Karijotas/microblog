package lt.karijotas.microblogging.service;

import java.util.List;
import java.util.Optional;

public abstract class GenericService {
    public List getAll() {
        return null;
    }

    public Optional getById(Long id) {
        return Optional.empty();
    }

    public Boolean deleteById(Long id) {
        return true;
    }

}
