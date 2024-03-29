package com.example.auth.domain.user;

public record RegisterDto(String login,String password,UserRole role) {
}
