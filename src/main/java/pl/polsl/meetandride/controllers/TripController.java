package pl.polsl.meetandride.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.meetandride.DTOs.TripDTO;
import pl.polsl.meetandride.DTOs.UserDTO;
import pl.polsl.meetandride.services.TripService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public void add(@RequestBody TripDTO tripDTO) {
        tripService.add(tripDTO);
    }


    @PutMapping
    public TripDTO edit(@RequestBody @Valid TripDTO tripDTO) {
        return tripService.edit(tripDTO);
    }


    @GetMapping
    public TripDTO getById(@RequestParam @NotNull Long id) {
        return tripService.getById(id);
    }


    @DeleteMapping
    public void delete(@RequestParam @NotNull Long id) {
        tripService.delete(id);
    }
}
