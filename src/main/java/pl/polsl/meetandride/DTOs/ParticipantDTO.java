package pl.polsl.meetandride.DTOs;

import lombok.Data;

@Data
public class ParticipantDTO {
    private String firstName;
    private String lastName;
    private MotorcycleDTO motorcycleDTO;
}
