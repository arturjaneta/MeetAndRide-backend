package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.Waypoint;

public interface WaypointRepository extends CrudRepository<Waypoint,Long> {
}
