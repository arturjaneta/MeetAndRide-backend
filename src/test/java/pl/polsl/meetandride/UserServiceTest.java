package pl.polsl.meetandride;


import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.exceptions.EmailAlreadyTakenException;
import pl.polsl.meetandride.repositories.UserRepository;
import pl.polsl.meetandride.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully(){
        final RegisterDTO registerDTO = new RegisterDTO("firstName","lastName","test@test.pl","test");

        given(userRepository.existsByEmail(registerDTO.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerNewUser(registerDTO);

        assertEquals(registerDTO.getFirstName(), result.getFirstName());
        assertEquals(registerDTO.getLastName(), result.getLastName());
        assertEquals(registerDTO.getEmail(), result.getEmail());
        assertNotNull(result.getPassword());
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowEmailAlreadyTakenException(){
        final RegisterDTO registerDTO = new RegisterDTO("firstName","lastName","test@test.pl","test");
        given(userRepository.existsByEmail(registerDTO.getEmail())).willReturn(true);

        assertThrows(EmailAlreadyTakenException.class,() -> {
            userService.registerNewUser(registerDTO);
        });
    }
}
