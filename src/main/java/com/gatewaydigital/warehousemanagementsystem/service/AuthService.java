package com.gatewaydigital.warehousemanagementsystem.service;

import com.gatewaydigital.warehousemanagementsystem.model.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface AuthService {

    public Map<String,Object> loginWithGoogle(String token) throws GeneralSecurityException, IOException;
}
