package com.gatewaydigital.warehousemanagementsystem.configuration;

import com.gatewaydigital.warehousemanagementsystem.model.User;
import com.gatewaydigital.warehousemanagementsystem.repository.UserRepository;
import com.gatewaydigital.warehousemanagementsystem.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try{
                email = jwtUtil.validateTokenAndGetEmail(token);
            }catch (Exception e){
                System.out.println("Invalid Jwt: "+e.getMessage());
            }

        }

        if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userRepository.findByEmail(email).orElse(null);

            if(user!=null){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,null, List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()))
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

    }
}
