package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.Booking;
import org.nzvirtual.api.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
