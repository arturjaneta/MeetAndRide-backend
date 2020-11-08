package pl.polsl.meetandride.exceptions.DTOs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.polsl.meetandride.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorDescDTO extends GenericErrorDescDTO {
    private List<ObjectErrorDescDTO> objectErrors = new ArrayList<>();
    private List<FieldErrorDescDTO> fieldErrors = new ArrayList<>();

    public ValidationErrorDescDTO(
            ErrorCode code,
            String msg,
            HttpStatus httpStatus
    ) {
        super(code, msg, httpStatus);
    }

    public ValidationErrorDescDTO(
            ErrorCode code,
            String msg,
            HttpStatus httpStatus,
            List<ObjectErrorDescDTO> objectErrors,
            List<FieldErrorDescDTO> fieldErrors
    ) {
        super(code, msg, httpStatus);
        this.objectErrors = objectErrors;
        this.fieldErrors = fieldErrors;
    }
}
