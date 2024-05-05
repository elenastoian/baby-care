package com.baby.care.controller;

import com.baby.care.controller.repsonse.AuthenticationResponse;
import com.baby.care.controller.repsonse.TokenConfirmationResponse;
import com.baby.care.controller.request.AuthenticationRequest;
import com.baby.care.controller.request.RegisterRequest;
import com.baby.care.service.security.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return authenticationService.register(request);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<TokenConfirmationResponse> confirm(@RequestParam("token") String token){
        return authenticationService.confirmToken(token);
    }
}
