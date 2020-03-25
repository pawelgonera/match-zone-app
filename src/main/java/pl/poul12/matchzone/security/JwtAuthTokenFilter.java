package pl.poul12.matchzone.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.poul12.matchzone.service.UserDetailsServiceImpl;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed pobraniem tokena z requesta " + new Date());

            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {

                System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed pobraniem username z tokena " + new Date());

                String username = tokenProvider.getUserNameFromJwtToken(jwt);

                System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed pobraniem Usera do UserDetails " + new Date());

                logger.info("username form tokenprovider: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed pobraniem użytkownika i uprawnień do UsernamePasswordAuthenticationToken" + new Date());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed ustawieniem detali" + new Date());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed ContextHolder" + new Date());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication -> Message: {}", e);
        }

        System.out.println("Jestem w JwtAuthTokenFilter.doFilterInternal przed wykonaniem filtra" + new Date());

        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {

        System.out.println("Jestem w JwtAuthTokenFilter.getJwt przed pobraniem tokena z headera" + new Date());

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            System.out.println("Jestem w JwtAuthTokenFilter.getJwt przed usunięciem Bearer z tokena" + new Date());

            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
