package de.planerio.developertest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SimpleObjectRepository extends CrudRepository<SimpleObject, Long> {
    SimpleObject findById(long id);
    List<SimpleObject> findByName(String name);
    List<SimpleObject> findAll();
}
