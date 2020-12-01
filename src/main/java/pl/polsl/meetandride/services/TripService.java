package pl.polsl.meetandride.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polsl.meetandride.DTOs.TripDTO;
import pl.polsl.meetandride.DTOs.WaypointDTO;
import pl.polsl.meetandride.entities.Trip;
import pl.polsl.meetandride.entities.Waypoint;
import pl.polsl.meetandride.exceptions.ResourceNotFoundException;
import pl.polsl.meetandride.repositories.TripRepository;
import pl.polsl.meetandride.repositories.WaypointRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserService userService;
    private final WaypointRepository waypointRepository;

    public TripDTO add(TripDTO tripDTO) {
        tripDTO.setOwnerId(userService.getCurrentUser().getId());
        Trip trip = tripRepository.save(toEntity(tripDTO));
        trip.setWaypoints(tripDTO.getWaypoints().stream().map(waypointDTO -> waypointRepository.save(new Waypoint(waypointDTO.getLat(),waypointDTO.getLng(),trip))).collect(Collectors.toList()));
        trip.getParticipants().add(userService.getCurrentUser());
        return toDTO(trip);
    }

    public TripDTO edit(TripDTO tripDTO) {
        Trip trip = findById(tripDTO.getId());
        fillEntityWithDtoData(trip,tripDTO);
        trip.setWaypoints(tripDTO.getWaypoints().stream().map(waypointDTO -> waypointRepository.save(new Waypoint(waypointDTO.getLat(),waypointDTO.getLng(),trip))).collect(Collectors.toList()));
        trip.setUpdateDateTime(LocalDateTime.now());
        return toDTO(tripRepository.save(trip));
    }

    public TripDTO getById(Long id) {
        return toDTO(findById(id));
    }

    public void delete(Long id) {
        tripRepository.deleteById(id);
    }

    public Trip findById(Long id) {
        Optional<Trip> optionalUser = tripRepository.findById(id);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new ResourceNotFoundException("Trip with id: " + id + " not exists in DB!");
    }

    private TripDTO toDTO(Trip trip){
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        tripDTO.setTitle(trip.getTitle());
        tripDTO.setDescription(trip.getDescription());
        tripDTO.setFromDate(trip.getFromDate());
        tripDTO.setToDate(trip.getToDate());
        tripDTO.setFromPlace(trip.getFromPlace());
        tripDTO.setToPlace(trip.getToPlace());
        tripDTO.setWaypoints(trip.getWaypoints().stream().map(waypoint -> new WaypointDTO(waypoint.getLat(),waypoint.getLat())).collect(Collectors.toList()));
        tripDTO.setSpeed(trip.getSpeed());
        tripDTO.setOwnerId(trip.getOwner().getId());
        return tripDTO;
    }

    private Trip toEntity(TripDTO tripDTO){
        Trip trip = new Trip();
        fillEntityWithDtoData(trip,tripDTO);
        return trip;
    }

    private void fillEntityWithDtoData(Trip trip,TripDTO tripDTO){
        trip.setTitle(tripDTO.getTitle());
        trip.setDescription(tripDTO.getDescription());
        trip.setFromDate(tripDTO.getFromDate());
        trip.setToDate(tripDTO.getToDate());
        trip.setFromPlace(tripDTO.getFromPlace());
        trip.setToPlace(tripDTO.getToPlace());
        trip.setSpeed(tripDTO.getSpeed());
        trip.setOwner(userService.findById(tripDTO.getOwnerId()));
    }

    public List<TripDTO> getMyTrips() {
        return tripRepository.findAllByOwner(userService.getCurrentUser()).stream().map(trip -> toDTO(trip)).collect(Collectors.toList());
    }

    public void addUserToTrip(Long id) {
        Trip trip = findById(id);
        trip.getParticipants().add(userService.getCurrentUser());
        tripRepository.save(trip);
    }

    public List<TripDTO> getAll(String date, String range, String speed,  String tags) {
        return tripRepository.findAll().stream().map(trip -> toDTO(trip)).collect(Collectors.toList());
    }
}
