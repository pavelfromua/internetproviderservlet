package my.project.internetprovider.service;

import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;

import java.util.List;

public interface GenericService<T, K> {
    T create(T element) throws ValidationException;

    T findById(K id) throws NotFoundException;

    List<T> findAll();

    void update(T element) throws UpdateException;

    void delete(K id);
}
