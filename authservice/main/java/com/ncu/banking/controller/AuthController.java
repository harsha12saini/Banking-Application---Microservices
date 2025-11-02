package com.ncu.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ncu.banking.dto.SignupDto;
import com.ncu.banking.dto.ReturnDto;
import com.ncu.banking.dto.AuthDto;
import com.ncu.banking.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService _AuthService;

    @Autowired
    public AuthController(AuthService authService) {
        this._AuthService = authService;
    }

    // ✅ Signup API with proper exception handling
    @PostMapping("/signup")
    public ResponseEntity<ReturnDto> signUp(@RequestBody SignupDto cred) {
        ReturnDto response = new ReturnDto();

        try {
            if (cred == null || cred.get_Email() == null || cred.get_Email().isEmpty()) {
                response.set_Status("Email is required.");
                response.set_Email("Unknown");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isSuccess = _AuthService.signup(cred, response);

            if (isSuccess) {
                return ResponseEntity.ok(response); // ✅ Status 200 OK
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception ex) {
            response.set_Email(cred != null ? cred.get_Email() : "Unknown");
            response.set_Status("Signup failed: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Authenticate API with try-catch
    @PostMapping("/authenticate")
    public ResponseEntity<ReturnDto> Authenticate(@RequestBody AuthDto cred) {
        ReturnDto response = new ReturnDto();

        try {
            if (cred == null || cred.get_Email() == null || cred.get_Email().isEmpty()) {
                response.set_Status("Email cannot be empty.");
                response.set_Email("Unknown");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isAuthenticated = _AuthService.authenticate(cred);
            response.set_Email(cred.get_Email());

            if (isAuthenticated) {
                response.set_Status("Authentication successful.");
                return ResponseEntity.ok(response);
            } else {
                response.set_Status("Authentication failed. Invalid credentials.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception ex) {
            response.set_Email(cred != null ? cred.get_Email() : "Unknown");
            response.set_Status("Authentication error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ (Optional) Simple health check or debug endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        try {
            return ResponseEntity.ok("Auth service is running fine.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Health check failed: " + ex.getMessage());
        }
    }
}
