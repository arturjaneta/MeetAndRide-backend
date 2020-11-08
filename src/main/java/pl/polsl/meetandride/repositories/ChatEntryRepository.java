package pl.polsl.meetandride.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.meetandride.entities.ChatEntry;

public interface ChatEntryRepository extends CrudRepository<ChatEntry,Long> {
}
