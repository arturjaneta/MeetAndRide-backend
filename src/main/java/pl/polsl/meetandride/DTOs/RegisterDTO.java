package pl.polsl.meetandride.DTOs;

import lombok.Data;

@Data
public class RegisterDTO {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String phoneNumber;
}
