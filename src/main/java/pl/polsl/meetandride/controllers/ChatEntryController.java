package pl.polsl.meetandride.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.meetandride.DTOs.ChatEntryDTO;
import pl.polsl.meetandride.services.ChatEntryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatentry")
public class ChatEntryController {
    private final ChatEntryService chatEntryService;

    @PostMapping
    public void add(@RequestBody ChatEntryDTO chatEntryDTO) {
        chatEntryService.add(chatEntryDTO);
    }


    @PutMapping
    public ChatEntryDTO edit(@RequestBody @Valid ChatEntryDTO chatEntryDTO) {
        return chatEntryService.edit(chatEntryDTO);
    }


    @GetMapping
    public ChatEntryDTO getById(@RequestParam @NotNull Long id) {
        return chatEntryService.getById(id);
    }


    @DeleteMapping
    public void delete(@RequestParam @NotNull Long id) {
        chatEntryService.delete(id);
    }
}
