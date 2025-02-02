package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "motorcycles")
public class Motorcycle extends BaseEntity{

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "power", nullable = false)
    private int power;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @OneToMany(mappedBy = "motorcycle",cascade = CascadeType.REMOVE)
    private Set<ParticipantMotorcycle> participantMotorcycles = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
    private User owner;
}
