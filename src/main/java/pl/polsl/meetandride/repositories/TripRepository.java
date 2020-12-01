package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.Trip;
import pl.polsl.meetandride.entities.User;

import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Long> {
    List<Trip> findAllByOwner(User owner);
    List<Trip> findAll();
}
