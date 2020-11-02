package pl.polsl.meetandride.security;

import com.google.common.base.Strings;
import pl.polsl.meetandride.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JwtUserDetailsService userDetailsService;

    private JwtUtils jwtUtils;

    public JwtAuthorizationFilter(JwtUserDetailsService userDetailsService,
                                  JwtUtils jwtUtils
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/token/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtUtils.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtUtils.getTokenPrefix())) {
                filterChain.doFilter(request, response);
                return;
        }
        String token = jwtUtils.extractTokenFromHeader(authorizationHeader);

        try {
            Claims claims = jwtUtils.extractAllClaims(token);
            String username = claims.getSubject();
            User user = (User) userDetailsService.loadUserByUsername(username);

            var authorities = (List<Map<String, String>>) claims.get("authorities");
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, simpleGrantedAuthorities)
            );

        } catch (ExpiredJwtException ex) {
            jwtUtils.createErrorResponse(response, 401, "The access token is expired!");
            return;
        } catch (JwtException e) {
            jwtUtils.createErrorResponse(response, 401, "The access token is invalid!");
            return;
        } catch (UsernameNotFoundException e) {
            jwtUtils.createErrorResponse(response, 401, "The access token is for non-existent user!");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
