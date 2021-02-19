package my.project.internetprovider.service;

import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;

import java.util.List;

public interface GenericService<T, K> {
    T create(T element);

    T findById(K id) throws NotFoundException;

    List<T> findAll();

    void update(T element);

    void delete(K id);
}
