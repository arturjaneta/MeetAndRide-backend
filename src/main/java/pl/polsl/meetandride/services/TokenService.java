package pl.polsl.meetandride.services;

import pl.polsl.meetandride.entities.RefreshToken;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.repositories.UserRepository;
import pl.polsl.meetandride.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public RefreshToken saveNewToken(User user, String token) {

        String hashedToken = DigestUtils.sha256Hex(token);

        RefreshToken tokenEntity = user.getRefreshToken() == null ? new RefreshToken() : user.getRefreshToken();
        tokenEntity.setHashedToken(hashedToken);
        tokenEntity.setUser(user);
        user.setRefreshToken(tokenEntity);

        return userRepository.save(user).getRefreshToken();

    }

    public boolean isValidToken(User user, String token) {
        return DigestUtils.sha256Hex(token).equals(user.getRefreshToken().getHashedToken());
    }

    @Transactional
    public ResponseEntity refresh() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newRefreshToken = jwtUtils.generateRefreshToken(currentUser);

        this.saveNewToken(currentUser, newRefreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(jwtUtils.getAuthorizationHeader(), jwtUtils.getTokenPrefix() + jwtUtils.generateAccessToken(currentUser));
        headers.add(jwtUtils.getTokenRefreshHeader(), jwtUtils.getTokenPrefix() + newRefreshToken);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

}
