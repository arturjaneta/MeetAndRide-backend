package pl.polsl.meetandride.services.bootstrapservices;

import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.enums.BootStrapLabel;
import pl.polsl.meetandride.exceptions.EmailAlreadyTakenException;
import pl.polsl.meetandride.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("development")
@Service
@RequiredArgsConstructor
public class DevelopmentBootStrapService extends BootStrapService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    @Override
    protected void writeDefaults() {
        super.writeDefaults();
        entryService.createIfNotExists(BootStrapLabel.CREATE_DEVELOP_ADMIN, this::createDevelopAdmin);
    }

    private void createDevelopAdmin() {

        log.info("Creating default development admin account...");
        User user = new User(
                "admin@admin.pl", bCryptPasswordEncoder.encode("admin"),
                "Admin", "Adminsson",
                true, "123456789",null
        );
        try {
            userService.addUser(user);
            log.info("Created.");
        } catch (EmailAlreadyTakenException e) {
            log.info("User with default e-mail already present. Skipping.");
        }

    }
}
