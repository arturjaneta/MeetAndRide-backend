package pl.polsl.meetandride.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "waypoints")
public class Waypoint extends BaseEntity {
    @Column(name = "lat", nullable = false)
    private double lat;
    @Column(name = "lng", nullable = false)
    private double lng;
    @ManyToOne
    @JoinColumn(name = "trip", referencedColumnName = "id", nullable = false)
    private Trip trip;
}
