package pl.polsl.meetandride.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.meetandride.DTOs.TripDTO;
import pl.polsl.meetandride.entities.Trip;
import pl.polsl.meetandride.exceptions.ResourceNotFoundException;
import pl.polsl.meetandride.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserService userService;

    public TripDTO add(TripDTO tripDTO) {
        return toDTO(tripRepository.save(toEntity(tripDTO)));
    }

    public TripDTO edit(TripDTO tripDTO) {
        Trip trip = findById(tripDTO.getId());
        fillEntityWithDtoData(trip,tripDTO);
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
        tripDTO.setTrace(trip.getTrace());
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
        trip.setTrace(tripDTO.getTrace());
        trip.setSpeed(tripDTO.getSpeed());
        trip.setOwner(userService.findById(tripDTO.getOwnerId()));
    }
}
