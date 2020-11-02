package pl.polsl.meetandride.repositories;

import pl.polsl.meetandride.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
