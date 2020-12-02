package pl.polsl.meetandride.DTOs;

import lombok.Data;
import pl.polsl.meetandride.enums.Speed;
import pl.polsl.meetandride.enums.Tags;

import java.util.List;

@Data
public class FindTripDTO {
    private Integer date;
    private Integer range;
    private WaypointDTO location;
    private List<Speed> speed;
    private List<Tags> tags;
}
