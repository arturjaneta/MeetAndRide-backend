package pl.polsl.meetandride.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tokens")
public class RefreshToken extends BaseEntity {

    @OneToOne(mappedBy = "refreshToken")
    private User user;

    @Column(name = "hashed_token")
    private String hashedToken;

}
