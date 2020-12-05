package pl.polsl.meetandride.controllers;


import pl.polsl.meetandride.DTOs.MotorcycleDTO;
import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.DTOs.UserDTO;
import pl.polsl.meetandride.entities.Motorcycle;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterDTO data){
        userService.registerNewUser(data);
    }

    @PostMapping("/saveall")
    public void saveAll(@RequestBody List<UserDTO> data){
        userService.saveAll(data);
    }

    @PostMapping("/addmotorcycle")
    public void addMotorcycle(@RequestBody MotorcycleDTO motorcycleDTO){
        userService.addMotorcycle(motorcycleDTO);
    }

    @PutMapping
    public UserDTO edit(@RequestBody @Valid UserDTO userDTO) {
        return userService.edit(userDTO);
    }

    @PatchMapping
    public void changePassword(String password) {
        userService.changePassword(password);
    }

    @PatchMapping("/name")
    public void changeName(@RequestParam String firstName,@RequestParam String lastName) {
        userService.changeName(firstName,lastName);
    }

    @GetMapping("/getcurrent")
    public UserDTO getCurrent() {
        return userService.getCurrent();
    }

    @GetMapping("/getmotorcycles")
    public List<MotorcycleDTO> getCurrentUserMotorcycles() {
        return userService.getMotorcycles();
    }

    @GetMapping("/getall")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getall/trip")
    public List<UserDTO> getAllByTrip(@RequestParam Long id) {
        return userService.getAllByTrip(id);
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
