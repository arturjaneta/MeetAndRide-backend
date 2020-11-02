package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class DatedEntity extends BaseEntity {
    @Column(name = "creation_date_time", nullable = false)
    private final LocalDateTime creationDateTime = LocalDateTime.now();
    @Column(name = "update_date_time", nullable = false)
    private LocalDateTime updateDateTime = LocalDateTime.now();
}
