package pl.polsl.meetandride.components;

import pl.polsl.meetandride.services.bootstrapservices.BootStrapService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class BootStrap {

    private final BootStrapService bootStrapService;


    @EventListener(ApplicationReadyEvent.class)
    private void init() {
        try {
            log.info("BootStrap start");
            bootStrapService.boot();
            log.info("BootStrap success");
        } catch (Exception e) {
            log.error("BootStrap failed," + e);
            e.printStackTrace();
            throw e;
        }
    }

}
