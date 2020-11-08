package pl.polsl.meetandride.DTOs;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    private String email;

    private boolean isActive;

    private boolean isAdmin;
}
