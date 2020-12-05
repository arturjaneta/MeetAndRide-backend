package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.Motorcycle;

public interface MotorcycleRepository extends CrudRepository<Motorcycle,Long> {
}
