package pl.polsl.meetandride.repositories;

import pl.polsl.meetandride.entities.BootStrapEntry;
import pl.polsl.meetandride.enums.BootStrapLabel;
import org.springframework.data.repository.CrudRepository;

public interface BootStrapEntryRepository extends CrudRepository<BootStrapEntry,Long> {
    boolean existsByLabel(BootStrapLabel label);
}
