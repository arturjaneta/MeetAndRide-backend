package pl.polsl.meetandride.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaypointDTO {
    private double lat;
    private double lng;
}
