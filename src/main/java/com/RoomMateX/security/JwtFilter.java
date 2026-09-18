package com.RoomMateX.security;

//These come from servlet API. Used to: read request, write response, pass control to next filter
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lombok generates constructor automatically for final fields no need of @Autowired
import lombok.RequiredArgsConstructor;

//Spring security authentication object. Represents logged-in user.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//Stores authenticated user globally per request
import org.springframework.security.core.context.SecurityContextHolder;

//Spring Security user abstraction
import org.springframework.security.core.userdetails.UserDetails;

//Register class as Spring bean
import org.springframework.stereotype.Component;

//Guarantees this filter runs once per HTTP request. Not twice.
import org.springframework.web.filter.OncePerRequestFilter;

//Checked exception
import java.io.IOException;

@Component
@RequiredArgsConstructor

//extends OncePerRequestFilter This filter executes before controller for EVERY request
public class JwtFilter extends OncePerRequestFilter {

    //jwtUtil is used to: extract email, validate token
    private final JwtUtil jwtUtil;
    //Loads user from DB
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    //this is the heart Called automatically for every request.
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");  //reads Authorization: Bearer TOKEN from HTTP header
        String path = req.getServletPath();
        if(path.startsWith("/api/aut")){
            chain.doFilter(req,res);
            return;
        }
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            String email = jwtUtil.extractEmail(token);

            // means: Token Contains email User no already authenticated. Avoid duplicate auth.
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                if(jwtUtil.validateToken(token))
                {
                    //Create Spring Security authentication Contains: user, role, credentials null(JWT already validated)
                    UsernamePasswordAuthenticationToken auth = new  UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //This is critical. User is logged in.
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        //pass request to: next filter, controller without this -> request stops.
        chain.doFilter(req, res);

    }

}
