package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.ParticipantMotorcycle;

import java.util.List;

public interface ParticipantMotorcycleRepository extends CrudRepository<ParticipantMotorcycle,Long> {
    List<ParticipantMotorcycle> findAllByTrip_Id(Long id);
    List<ParticipantMotorcycle> findAllByTrip_IdAndUser_Id(Long tripId,Long userId);
}
