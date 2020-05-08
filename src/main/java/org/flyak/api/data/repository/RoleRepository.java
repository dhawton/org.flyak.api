package org.flyak.api.data.repository;

import org.flyak.api.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<User, Long> {
}
