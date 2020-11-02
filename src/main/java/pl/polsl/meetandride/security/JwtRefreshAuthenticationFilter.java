package pl.polsl.meetandride.security;

import com.google.common.base.Strings;
import pl.polsl.meetandride.entities.User;
import pl.polsl.meetandride.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtRefreshAuthenticationFilter extends OncePerRequestFilter {

    private JwtUserDetailsService userDetailsService;

    private JwtUtils jwtUtils;

    private TokenService tokenService;

    public JwtRefreshAuthenticationFilter(JwtUserDetailsService userDetailsService,
                                          JwtUtils jwtUtils,
                                          TokenService tokenService
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/token/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String refreshTokenHeader = request.getHeader(jwtUtils.getTokenRefreshHeader());
        if (Strings.isNullOrEmpty(refreshTokenHeader) || !refreshTokenHeader.startsWith(jwtUtils.getTokenPrefix())) {
            jwtUtils.createErrorResponse(response, 400, "The refresh token is missing or missing token prefix!");
            return;
        }
        String refreshToken = jwtUtils.extractTokenFromHeader(refreshTokenHeader);

        try {
            Claims claims = jwtUtils.extractAllClaims(refreshToken);
            String username = claims.getSubject();
            User user = (User) userDetailsService.loadUserByUsername(username);

            if (!tokenService.isValidToken(user, refreshToken)) {
                jwtUtils.createErrorResponse(response, 401, "The refresh token was not recognized by the server!");
                return;
            }

            var authorities = (List<Map<String, String>>) claims.get("authorities");
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, simpleGrantedAuthorities)
            );

        } catch (JwtException e) {
            jwtUtils.createErrorResponse(response, 401, "The refresh token is expired or invalid!");
            return;
        } catch (UsernameNotFoundException e) {
            jwtUtils.createErrorResponse(response, 401, "The refresh token is for non-existent user!");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
