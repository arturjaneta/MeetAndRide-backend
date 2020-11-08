package pl.polsl.meetandride.services;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.DTOs.UserDTO;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.exceptions.EmailAlreadyTakenException;
import pl.polsl.meetandride.exceptions.ResourceNotFoundException;
import pl.polsl.meetandride.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerNewUser(RegisterDTO data) {
        if (userRepository.existsByEmail(data.getEmail()))
            throw new EmailAlreadyTakenException("A user with the same e-mail address is already present in the DB!");
        User newUser = new User();
        newUser.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
        newUser.setFirstName(data.getFirstName());
        newUser.setLastName(data.getLastName());
        newUser.setEmail(data.getEmail());

        return userRepository.save(newUser);
    }

    public UserDTO edit(UserDTO userDTO) {
        User user = findById(userDTO.getId());
        fillEntityWithDtoData(user,userDTO);
        user.setUpdateDateTime(LocalDateTime.now());
        return toDTO(userRepository.save(user));
    }

    public UserDTO getById(Long id) {
        return toDTO(findById(id));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(user -> toDTO(user)).collect(Collectors.toList());
    }

    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new ResourceNotFoundException("User with id: " + id + " not exists in DB!");
    }

    private UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setActive(user.isActive());
        userDTO.setAdmin(user.isAdmin());
        return userDTO;
    }

    private User toEntity(UserDTO userDTO){
        User user = new User();
        fillEntityWithDtoData(user,userDTO);
        return user;
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void fillEntityWithDtoData(User user,UserDTO userDTO){
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUpdateDateTime(LocalDateTime.now());
        if(getCurrentUser().isAdmin() == true) {
            user.setActive(userDTO.isActive());
            user.setAdmin(user.isAdmin());
        }
    }


}
