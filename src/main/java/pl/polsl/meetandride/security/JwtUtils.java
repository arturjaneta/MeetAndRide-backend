package pl.polsl.meetandride.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

@Component
@PropertySource("classpath:application.properties")
public class JwtUtils {

    @Value("${application.jwt.header}")
    private String AUTHORIZATION_HEADER;

    @Value("${application.jwt.tokenrefreshheader}")
    private String TOKEN_REFRESH_HEADER;

    @Value("${application.jwt.prefix}")
    private String TOKEN_PREFIX;

    @Value("${application.jwt.secret}")
    private String SECRET_KEY;

    @Value("${application.jwt.accesstokenexpiration}")
    private long JWT_ACCESS_TOKEN_VALIDITY;

    @Value("${application.jwt.refreshtokenexpiration}")
    private long JWT_REFRESH_TOKEN_VALIDITY;

    public String getAuthorizationHeader() {
        return this.AUTHORIZATION_HEADER;
    }

    public String getTokenRefreshHeader() {
        return this.TOKEN_REFRESH_HEADER;
    }

    public String getTokenPrefix() {
        return this.TOKEN_PREFIX;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTokenFromHeader(String headerString) {
        return headerString.startsWith(TOKEN_PREFIX) ? headerString.substring(TOKEN_PREFIX.length()) : null;
    }

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(JWT_ACCESS_TOKEN_VALIDITY).toInstant()))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(JWT_REFRESH_TOKEN_VALIDITY).toInstant()))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public void createErrorResponse(HttpServletResponse response, int status, String msg) throws IOException {
        response.getOutputStream().print("Security Error: " + msg);
        response.setStatus(status);
    }

}
