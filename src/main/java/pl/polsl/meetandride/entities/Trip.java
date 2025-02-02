package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.meetandride.DTOs.WaypointDTO;
import pl.polsl.meetandride.enums.Speed;
import pl.polsl.meetandride.enums.Tags;

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

    @OneToMany(mappedBy = "trip",cascade = CascadeType.REMOVE)
    private List<Waypoint> waypoints;

    @OneToMany(mappedBy = "trip",cascade = CascadeType.REMOVE)
    private Set<ParticipantMotorcycle> participantMotorcycles = new LinkedHashSet<>();

    @Column(name = "speed", nullable = false)
    private Speed speed;

    @ElementCollection(targetClass = Tags.class)
    @JoinTable(name = "tags_table", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "tags", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Set<Tags> tags = new LinkedHashSet<>();

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

    @OneToMany(mappedBy = "trip",cascade = CascadeType.REMOVE)
    private Set<ChatEntry> chatEntries = new LinkedHashSet<>();

}
