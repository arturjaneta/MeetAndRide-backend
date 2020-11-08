package pl.polsl.meetandride.exceptions.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorDescDTO {
    private String name;
    private Object value;
    private String error;
}
