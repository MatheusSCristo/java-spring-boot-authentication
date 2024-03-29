package com.example.auth.controllers;

import com.example.auth.domain.user.*;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword=  new UsernamePasswordAuthenticationToken(data.login(),data.password());
        var auth= this.authenticationManager.authenticate(usernamePassword);
        var token=tokenService.generateToken((User)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto registerDto){
        if(userRepository.findByLogin(registerDto.login())!=null) return ResponseEntity.badRequest().build();
        String encryptedPassword= new BCryptPasswordEncoder().encode(registerDto.password());
        User user=new User(registerDto.login(), encryptedPassword, registerDto.role());
        userRepository.save(user);
        return ResponseEntity.ok().build();

    }



}
