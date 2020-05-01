package org.flyak.api.data.repository;

import org.flyak.api.data.entity.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
    Optional<Airport> findByIcao(String icao);
}
