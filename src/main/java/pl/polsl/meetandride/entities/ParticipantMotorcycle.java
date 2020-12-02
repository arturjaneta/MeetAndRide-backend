package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "participants_motorcycles")
public class ParticipantMotorcycle extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "trip", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @JoinColumn(name = "motorcycle", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Motorcycle motorcycle;
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User user;
}
