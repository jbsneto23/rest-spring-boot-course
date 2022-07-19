package br.com.jbsneto.controllers;

import br.com.jbsneto.data.dto.v1.security.AccountCredentialsDTO;
import br.com.jbsneto.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsDTO dto) {
        if (checkIfAccountCredentialsDTOIsInvalid(dto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authServices.signin(dto);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return token;
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (checkIfUsernameAndRefreshTokenAreNull(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authServices.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return token;
    }

    private boolean checkIfUsernameAndRefreshTokenAreNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private boolean checkIfAccountCredentialsDTOIsInvalid(AccountCredentialsDTO dto) {
        return dto == null || dto.getUsername() == null || dto.getUsername().isBlank() || dto.getPassword() == null || dto.getPassword().isBlank();
    }

}
