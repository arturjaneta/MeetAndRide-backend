package pl.polsl.meetandride.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.services.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UsernamePasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernamePasswordAuthenticationRequest.class);

            User authenticatedUser = (User) jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            if(!authenticatedUser.isActive()) {
                throw new AuthenticationException("Your account is banned!") {
                };
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticatedUser, authenticationRequest.getPassword()
            ));
        } catch (IOException e) {
            throw new AuthenticationException("Invalid username and/or password!") {
            };
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult
    ) {

        String accessToken = jwtUtils.generateAccessToken((UserDetails) authResult.getPrincipal());
        String refreshToken = jwtUtils.generateRefreshToken((UserDetails) authResult.getPrincipal());
        response.addHeader(jwtUtils.getAuthorizationHeader(), jwtUtils.getTokenPrefix() + accessToken);
        response.addHeader(jwtUtils.getTokenRefreshHeader(), jwtUtils.getTokenPrefix() + refreshToken);

        // For informational purposes only
        try {
            response.addHeader("AccessTokenExpiration", jwtUtils.extractExpirationDate(accessToken).toString());
        } catch (ExpiredJwtException e) {
            response.addHeader("AccessTokenExpiration", "0");
        }
        try {
            response.addHeader("RefreshTokenExpiration", jwtUtils.extractExpirationDate(refreshToken).toString());
        } catch (ExpiredJwtException e) {
            response.addHeader("RefreshTokenExpiration", "0");
        }

        User user = (User) authResult.getPrincipal();
        tokenService.saveNewToken(user, refreshToken);
    }
}
