package pl.polsl.meetandride.services.bootstrapservices;

import pl.polsl.meetandride.entities.BootStrapEntry;
import pl.polsl.meetandride.enums.BootStrapLabel;
import pl.polsl.meetandride.repositories.BootStrapEntryRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class BootStrapEntryService {

    private final BootStrapEntryRepository bootStrapEntryRepository;

    public void createIfNotExists(BootStrapLabel label, Runnable runnable) {
        String entryStatus = "already in db";
        boolean entryExists = existsByLabel(label);

        if(!entryExists) {
            runnable.run();
            create(label);
            entryStatus = "creating";
        }

        log(label, entryStatus);
    }

    public boolean existsByLabel(BootStrapLabel label) {
        return bootStrapEntryRepository.existsByLabel(label);
    }

    public BootStrapEntry create(BootStrapLabel label) {
        BootStrapEntry bootStrapEntry = new BootStrapEntry();
        bootStrapEntry.setLabel(label);
        return bootStrapEntryRepository.save(bootStrapEntry);
    }

    private void log(BootStrapLabel label, String entryStatus) {
        String entryMessage = "processing " + label + " -> " + entryStatus;
        log.info(entryMessage);
    }


}