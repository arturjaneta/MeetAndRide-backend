package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.meetandride.DTOs.WaypointDTO;
import pl.polsl.meetandride.enums.Speed;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "trips")
public class Trip extends DatedEntity
{
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "from_date", nullable = false)
    private LocalDateTime fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDateTime toDate;

    @Column(name = "from_place", nullable = false)
    private String fromPlace;

    @Column(name = "to_place", nullable = false)
    private String toPlace;

    @OneToMany(mappedBy = "trip")
    private List<Waypoint> waypoints;

    @Column(name = "speed", nullable = false)
    private Speed speed;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "trip_participants",
            joinColumns = @JoinColumn(name = "trip_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> participants = new LinkedHashSet<>();

    @OneToMany(mappedBy = "trip")
    private Set<ChatEntry> chatEntries = new LinkedHashSet<>();

}
