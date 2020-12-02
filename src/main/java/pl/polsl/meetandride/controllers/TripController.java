package pl.polsl.meetandride.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.meetandride.DTOs.FindTripDTO;
import pl.polsl.meetandride.DTOs.TripDTO;
import pl.polsl.meetandride.DTOs.WaypointDTO;
import pl.polsl.meetandride.enums.Speed;
import pl.polsl.meetandride.enums.Tags;
import pl.polsl.meetandride.services.TripService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
@CrossOrigin
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

    @PatchMapping
    public void addUserToTrip(@RequestParam Long id) {
        tripService.addUserToTrip(id);
    }

    @GetMapping
    public TripDTO getById(@RequestParam @NotNull Long id) {
        return tripService.getById(id);
    }

    @PostMapping("/all")
    public List<TripDTO> getAll(@RequestBody FindTripDTO findTripDTO) {
        return tripService.getAll(findTripDTO);
    }

    @GetMapping("/mytrips")
    public List<TripDTO> getMyTrips() {
        return tripService.getMyTrips();
    }

    @DeleteMapping
    public void delete(@RequestParam @NotNull Long id) {
        tripService.delete(id);
    }
}
