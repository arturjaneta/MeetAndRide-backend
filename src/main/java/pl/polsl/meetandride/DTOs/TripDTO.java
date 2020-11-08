package pl.polsl.meetandride.DTOs;

import lombok.Data;
import pl.polsl.meetandride.enums.Speed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TripDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime fromDate;
    @NotNull
    private LocalDateTime toDate;
    @NotEmpty
    private String fromPlace;
    @NotEmpty
    private String toPlace;
    @NotEmpty
    private String trace;
    @NotNull
    private Speed speed;
    @NotNull
    private Long ownerId;
}
