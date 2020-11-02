package pl.polsl.meetandride.controllers;


import pl.polsl.meetandride.DTOs.RegisterDTO;
import pl.polsl.meetandride.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
