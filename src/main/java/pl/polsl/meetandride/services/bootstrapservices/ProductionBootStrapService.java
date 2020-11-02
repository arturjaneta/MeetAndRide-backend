package pl.polsl.meetandride.services.bootstrapservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("production")
@Service
public class ProductionBootStrapService extends BootStrapService {
    @Override
    protected void writeDefaults() {
        super.writeDefaults();
    }
}
