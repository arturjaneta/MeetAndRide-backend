package pl.polsl.meetandride.services.bootstrapservices;

import pl.polsl.meetandride.DTOs.RegisterDTO;
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
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("a@a.pl");
        registerDTO.setFirstName("Admin");
        registerDTO.setLastName("Admin");
        registerDTO.setPassword("admin");
        try {
            userService.registerNewUser(registerDTO);
            log.info("Created.");
        } catch (EmailAlreadyTakenException e) {
            log.info("User with default e-mail already present. Skipping.");
        }

    }
}
