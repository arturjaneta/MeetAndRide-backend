package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.Trip;

public interface TripRepository extends CrudRepository<Trip, Long> {
}
