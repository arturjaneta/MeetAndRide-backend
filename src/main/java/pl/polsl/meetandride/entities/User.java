package pl.polsl.meetandride.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends DatedEntity implements UserDetails {

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private Set<Motorcycle> motorcycles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<ChatEntry> chatEntries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Set<ParticipantMotorcycle> participantMotorcycles = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Trip> tripsAsParticipant = new LinkedHashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Trip> tripsAsOwner = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refreshToken_id",
            referencedColumnName = "id",
            unique = true
    )
    private RefreshToken refreshToken = null;

    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }

    public String getFullNameInversed() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new LinkedHashSet<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(
                //TODO: IMPLEMENT PROPERLY
                "ROLE_ADMIN"
        )));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
