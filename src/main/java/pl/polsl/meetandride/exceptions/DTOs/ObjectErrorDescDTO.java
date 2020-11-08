package pl.polsl.meetandride.exceptions.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectErrorDescDTO {
    private String name;
    private String error;
}
