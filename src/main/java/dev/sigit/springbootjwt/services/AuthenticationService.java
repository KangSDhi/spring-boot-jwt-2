package dev.sigit.springbootjwt.services;

import dev.sigit.springbootjwt.dto.JwtAuthenticationResponse;
import dev.sigit.springbootjwt.dto.RefreshTokenRequest;
import dev.sigit.springbootjwt.dto.SignUpRequest;
import dev.sigit.springbootjwt.dto.SigninRequest;
import dev.sigit.springbootjwt.entities.User;

public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
