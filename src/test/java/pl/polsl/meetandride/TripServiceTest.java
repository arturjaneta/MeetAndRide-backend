package pl.polsl.meetandride;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.polsl.meetandride.repositories.TripRepository;
import pl.polsl.meetandride.repositories.WaypointRepository;
import pl.polsl.meetandride.services.TripService;
import pl.polsl.meetandride.services.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private TripRepository tripRepository;
    @Mock
    private UserService userService;
    @Mock
    private WaypointRepository waypointRepository;
    @InjectMocks
    private TripService tripService;

    @Test
    void distanceTest(){
        double lat1=50.0955793;
        double lng1=18.5419933;
        double lat2=50.294113;
        double lng2=18.6657306;

        long result = tripService.distance(lat1,lng1,lat2,lng2);

        assertTrue(result>23000);
        assertTrue(result<24000);
    }
}
