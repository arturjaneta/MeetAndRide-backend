package pl.polsl.meetandride.controllers;

import pl.polsl.meetandride.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/refresh")
    public ResponseEntity refresh() {
        return tokenService.refresh();
    }

}
