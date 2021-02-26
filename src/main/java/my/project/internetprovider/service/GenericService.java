package my.project.internetprovider.service;

import my.project.internetprovider.exception.CheckException;
import java.util.List;

public interface GenericService<T, K> {
    T create(T element) throws CheckException;

    T findById(K id) throws CheckException;

    List<T> findAll();

    void update(T element) throws CheckException;

    void delete(K id);
}
