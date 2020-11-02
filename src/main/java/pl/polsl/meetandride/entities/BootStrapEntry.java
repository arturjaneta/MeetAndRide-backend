package pl.polsl.meetandride.entities;

import pl.polsl.meetandride.enums.BootStrapLabel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="bootstrapentries")
public class BootStrapEntry extends BaseEntity {

    @Column(name = "label")
    private BootStrapLabel label;
}
