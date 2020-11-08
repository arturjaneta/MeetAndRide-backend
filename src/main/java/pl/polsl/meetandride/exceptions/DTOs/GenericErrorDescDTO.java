package pl.polsl.meetandride.exceptions.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.polsl.meetandride.enums.ErrorCode;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericErrorDescDTO {
    private ErrorCode code;
    private String msg;
    private HttpStatus httpStatus;
}
