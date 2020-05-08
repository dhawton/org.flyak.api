package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<User, Long> {
}
