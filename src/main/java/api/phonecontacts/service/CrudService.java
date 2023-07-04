package api.phonecontacts.service;

import java.util.Set;
import java.util.UUID;

public interface CrudService <T> {
    public T save(T object);

    public Set<T> findAll();

    public T findById(UUID id);

    public void deleteById(UUID id);
}
