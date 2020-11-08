package pl.polsl.meetandride.DTOs;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChatEntryDTO {
    private Long id;
    @NotEmpty
    private String content;
    @NotNull
    private Long authorId;
    @NotNull
    private Long tripId;
}
