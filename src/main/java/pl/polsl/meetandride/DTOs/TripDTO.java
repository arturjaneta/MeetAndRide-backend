package pl.polsl.meetandride.DTOs;

import lombok.Data;
import pl.polsl.meetandride.enums.Speed;
import pl.polsl.meetandride.enums.Tags;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<WaypointDTO> waypoints;
    @NotNull
    private Speed speed;

    @NotNull
    private List<Tags> tags;

    private Long ownerId;
}
