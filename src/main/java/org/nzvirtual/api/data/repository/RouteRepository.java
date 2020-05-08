package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
}
