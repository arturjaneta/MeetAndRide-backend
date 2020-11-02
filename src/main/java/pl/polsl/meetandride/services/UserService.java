package pl.polsl.meetandride.services;

import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.exceptions.EmailAlreadyTakenException;
import pl.polsl.meetandride.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User addUser(@NonNull User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new EmailAlreadyTakenException("A user with the same e-mail address is already present in the DB!");
        return userRepository.save(user);
    }

    public User registerNewUser(RegisterDTO data) {
        if (userRepository.existsByEmail(data.getEmail()))
            throw new EmailAlreadyTakenException("A user with the same e-mail address is already present in the DB!");
        User newUser = new User();
        newUser.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
        newUser.setFirstName(data.getFirstName());
        newUser.setLastName(data.getLastName());
        newUser.setPhoneNumber(data.getPhoneNumber());
        newUser.setEmail(data.getEmail());

        return userRepository.save(newUser);
    }

}
