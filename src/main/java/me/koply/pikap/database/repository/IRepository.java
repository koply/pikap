package me.koply.pikap.database.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    T save(T t);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}
