package com.gatewaydigital.warehousemanagementsystem.service;

import com.gatewaydigital.warehousemanagementsystem.model.Role;
import com.gatewaydigital.warehousemanagementsystem.model.User;
import com.gatewaydigital.warehousemanagementsystem.repository.UserRepository;
import com.gatewaydigital.warehousemanagementsystem.utility.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${google.OAuth.client_id}")
    private String CLIENT_ID;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Map<String,Object> loginWithGoogle(String token) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        ).
                setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);

        if(idToken==null){
            throw new RuntimeException("Invalid ID token.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture =  (String) payload.get("picture");

        User user = userRepository.findByEmail(email).orElseGet(()->{
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setEmail(email);
            newUser.setPicture(picture);
            newUser.setName(name);
            newUser.setRole(Role.STAFF);
            return newUser;
        });

        String jwt = jwtUtil.generateToken(user.getEmail(),user.getRole().name());
        return Map.of("Token",token,"user",user);
    }
}
