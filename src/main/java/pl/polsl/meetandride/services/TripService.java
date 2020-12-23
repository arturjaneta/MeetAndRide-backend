package pl.polsl.meetandride.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.meetandride.DTOs.*;
import pl.polsl.meetandride.entities.ParticipantMotorcycle;
import pl.polsl.meetandride.entities.Trip;
import pl.polsl.meetandride.entities.Waypoint;
import pl.polsl.meetandride.exceptions.ResourceNotFoundException;
import pl.polsl.meetandride.repositories.MotorcycleRepository;
import pl.polsl.meetandride.repositories.ParticipantMotorcycleRepository;
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
    private final MotorcycleRepository motorcycleRepository;
    private final ParticipantMotorcycleRepository participantMotorcycleRepository;

    public TripDTO add(TripDTO tripDTO) {
        tripDTO.setOwnerId(userService.getCurrentUser().getId());
        Trip trip = tripRepository.save(toEntity(tripDTO));
        trip.setWaypoints(tripDTO.getWaypoints().stream().map(waypointDTO -> waypointRepository.save(new Waypoint(waypointDTO.getLat(), waypointDTO.getLng(), trip))).collect(Collectors.toList()));
        trip.getParticipants().add(userService.getCurrentUser());
        return toDTO(trip);
    }

    public TripDTO edit(TripDTO tripDTO) {
        Trip trip = findById(tripDTO.getId());
        trip.getWaypoints().forEach(waypoint -> waypointRepository.delete(waypoint));
        fillEntityWithDtoData(trip, tripDTO);
        trip.setWaypoints(tripDTO.getWaypoints().stream().map(waypointDTO -> waypointRepository.save(new Waypoint(waypointDTO.getLat(), waypointDTO.getLng(), trip))).collect(Collectors.toList()));
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

    private TripDTO toDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        tripDTO.setTitle(trip.getTitle());
        tripDTO.setDescription(trip.getDescription());
        tripDTO.setFromDate(trip.getFromDate());
        tripDTO.setToDate(trip.getToDate());
        tripDTO.setFromPlace(trip.getFromPlace());
        tripDTO.setToPlace(trip.getToPlace());
        tripDTO.setWaypoints(trip.getWaypoints().stream().map(waypoint -> new WaypointDTO(waypoint.getLat(), waypoint.getLng())).collect(Collectors.toList()));
        tripDTO.setSpeed(trip.getSpeed());
        tripDTO.setOwnerId(trip.getOwner().getId());
        tripDTO.setTags(trip.getTags().stream().collect(Collectors.toList()));
        return tripDTO;
    }

    private Trip toEntity(TripDTO tripDTO) {
        Trip trip = new Trip();
        fillEntityWithDtoData(trip, tripDTO);
        return trip;
    }

    private void fillEntityWithDtoData(Trip trip, TripDTO tripDTO) {
        trip.setTitle(tripDTO.getTitle());
        trip.setDescription(tripDTO.getDescription());
        trip.setFromDate(tripDTO.getFromDate());
        trip.setToDate(tripDTO.getToDate());
        trip.setFromPlace(tripDTO.getFromPlace());
        trip.setToPlace(tripDTO.getToPlace());
        trip.setSpeed(tripDTO.getSpeed());
        trip.setTags(tripDTO.getTags().stream().collect(Collectors.toSet()));
        trip.setOwner(userService.findById(tripDTO.getOwnerId()));
    }

    public List<TripDTO> getMyTrips() {
        return tripRepository.findAllByOwner(userService.getCurrentUser()).stream().map(trip -> toDTO(trip)).collect(Collectors.toList());
    }

    public void addUserToTrip(Long id,Long motorcycleId) {
        Trip trip = findById(id);
        trip.getParticipants().add(userService.getCurrentUser());
        ParticipantMotorcycle participantMotorcycle = new ParticipantMotorcycle();
        participantMotorcycle.setUser(userService.getCurrentUser());
        participantMotorcycle.setMotorcycle(motorcycleRepository.findById(motorcycleId).orElseThrow(()->new ResourceNotFoundException("Motorcycle with id:" + motorcycleId + " not exists in DB!")));
        participantMotorcycle.setTrip(trip);
        participantMotorcycleRepository.save(participantMotorcycle);
        tripRepository.save(trip);
    }



    public long distance(double lat1, double lng1, double lat2, double lng2) {
        int R = 6371000; // metres
        double temp1 = lat1 * Math.PI / 180;
        double temp2 = lat2 * Math.PI / 180;
        double temp3 = (lat2 - lat1) * Math.PI / 180;
        double temp4 = (lng2 - lng1) * Math.PI / 180;

        double a = Math.sin(temp3 / 2) * Math.sin(temp3 / 2) +
                Math.cos(temp1) * Math.cos(temp2) *
                        Math.sin(temp4 / 2) * Math.sin(temp4 / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c; // in metres
        return (long) d;
    }

    public List<TripDTO> getAll(FindTripDTO findTripDTO) {
        List<Trip> trips = tripRepository.findAll();
        trips.removeIf(trip -> !trip.getOwner().isActive());
        if (findTripDTO.getDate() != null) {
            if (findTripDTO.getDate() == 0) {
                trips.removeIf(trip -> trip.getFromDate().isBefore(LocalDateTime.now().plusDays(30)));
            } else {
                trips.removeIf(trip -> trip.getFromDate().isAfter(LocalDateTime.now().plusDays(findTripDTO.getDate())) || trip.getFromDate().isBefore(LocalDateTime.now()));
            }
        }
        if (findTripDTO.getRange() != null && findTripDTO.getLocation()!=null) {
            if(findTripDTO.getRange()==0)
                trips.removeIf(trip -> {
                            if(trip.getWaypoints().size()<1)
                                return true;
                            else
                            return 50000 > distance(
                                    trip.getWaypoints().get(0).getLat(),
                                    trip.getWaypoints().get(0).getLng(),
                                    findTripDTO.getLocation().getLat(),
                                    findTripDTO.getLocation().getLng());
                        }
                );
            else
                trips.removeIf(trip -> {
                    if (trip.getWaypoints().size() < 1)
                        return true;
                    else
                        return findTripDTO.getRange() < distance(
                                trip.getWaypoints().get(0).getLat(),
                                trip.getWaypoints().get(0).getLng(),
                                findTripDTO.getLocation().getLat(),
                                findTripDTO.getLocation().getLng());
                }
                );

        }
        if (findTripDTO.getSpeed() != null) {
            trips.removeIf(trip -> !findTripDTO.getSpeed().contains(trip.getSpeed()));
        }
        if (findTripDTO.getTags() != null) {
            trips.removeIf(trip -> !trip.getTags().containsAll(findTripDTO.getTags()));
        }
        return trips.stream().map(trip -> toDTO(trip)).collect(Collectors.toList());
    }

    public List<ParticipantDTO> getPartcipants(Long id) {
        return participantMotorcycleRepository.findAllByTrip_Id(id).stream().map(participantMotorcycle -> {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setFirstName(participantMotorcycle.getUser().getFirstName());
            participantDTO.setUserId(participantMotorcycle.getUser().getId());
            participantDTO.setLastName(participantMotorcycle.getUser().getLastName());
            MotorcycleDTO motorcycleDTO = new MotorcycleDTO();
            motorcycleDTO.setId(participantMotorcycle.getMotorcycle().getId());
            motorcycleDTO.setYear(participantMotorcycle.getMotorcycle().getYear());
            motorcycleDTO.setRegistrationNumber(participantMotorcycle.getMotorcycle().getRegistrationNumber());
            motorcycleDTO.setPower(participantMotorcycle.getMotorcycle().getPower());
            motorcycleDTO.setCapacity(participantMotorcycle.getMotorcycle().getCapacity());
            motorcycleDTO.setModelName(participantMotorcycle.getMotorcycle().getModelName());
            motorcycleDTO.setBrandName(participantMotorcycle.getMotorcycle().getBrandName());
            participantDTO.setMotorcycleDTO(motorcycleDTO);
            return participantDTO;
        }).collect(Collectors.toList());
    }

    public void removeUserFromTrip(Long id) {
        List<ParticipantMotorcycle> participantMotorcycles = participantMotorcycleRepository.findAllByTrip_IdAndUser_Id(id,userService.getCurrentUser().getId());
        Trip trip = findById(id);
        trip.getParticipants().removeIf(user -> user.getId()==userService.getCurrentUser().getId());
        tripRepository.save(trip);
        participantMotorcycleRepository.deleteAll(participantMotorcycles);
    }
}
