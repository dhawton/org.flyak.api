package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByPath(String path);
    @Query("SELECT f FROM File f WHERE CONCAT(f.path, f.name) = :expiredDate")
    Optional<File> findByKey(String key);
}
