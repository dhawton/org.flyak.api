package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    Optional<Equipment> findByIcao(String icao);
}
