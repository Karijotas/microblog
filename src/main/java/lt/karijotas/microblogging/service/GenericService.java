package lt.karijotas.microblogging.service;

import lt.karijotas.microblogging.model.User;

import java.util.List;
import java.util.Optional;

public abstract class GenericService {
    public List getAll() {
        return null;
    }

    public Optional getById(Long id) {
        return null;
    }

    public Boolean deleteById(Long id) {
        return true;
    }

}
