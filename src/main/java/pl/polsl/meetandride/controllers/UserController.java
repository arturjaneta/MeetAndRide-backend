package pl.polsl.meetandride.controllers;


import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.DTOs.UserDTO;
import pl.polsl.meetandride.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterDTO data){
        userService.registerNewUser(data);
    }


    @PutMapping
    public UserDTO edit(@RequestBody @Valid UserDTO userDTO) {
        return userService.edit(userDTO);
    }

    @GetMapping("/getall")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping
    public UserDTO getById(@RequestParam @NotNull Long id) {
        return userService.getById(id);
    }


    @DeleteMapping
    public void delete(@RequestParam @NotNull Long id) {
        userService.delete(id);
    }
}
