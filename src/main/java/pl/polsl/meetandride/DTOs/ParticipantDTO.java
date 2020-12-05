package pl.polsl.meetandride.DTOs;

import lombok.Data;

@Data
public class ParticipantDTO {
    private long userId;
    private String firstName;
    private String lastName;
    private MotorcycleDTO motorcycleDTO;
}
