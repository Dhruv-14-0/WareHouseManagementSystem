package com.gatewaydigital.warehousemanagementsystem.controller;

import com.gatewaydigital.warehousemanagementsystem.model.ApiResponse;
import com.gatewaydigital.warehousemanagementsystem.model.User;
import com.gatewaydigital.warehousemanagementsystem.repository.UserRepository;
import com.gatewaydigital.warehousemanagementsystem.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse> googleLogin(@RequestBody Map<String,String> body) throws GeneralSecurityException, IOException {
        Map<String, Object> tokenEmail = authService.loginWithGoogle(body.get("token"));

        ApiResponse response = new ApiResponse();
        response.setData(tokenEmail);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
