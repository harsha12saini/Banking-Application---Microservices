package com.ncu.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ncu.banking.dto.SignupDto;
import com.ncu.banking.dto.ReturnDto;
import com.ncu.banking.dto.AuthDto;
import com.ncu.banking.repository.AuthRepository;
import com.ncu.banking.exceptions.UserAlreadyExistsException;
import com.ncu.banking.exceptions.UserNotFoundException;
import com.ncu.banking.exceptions.DatabaseConnectionException;
@Service
public class AuthService {

    private final AuthRepository _AuthRepository;
    private final PasswordEncoder _PasswordEncoder;    

    @Autowired
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this._AuthRepository = authRepository;
        this._PasswordEncoder = passwordEncoder;
    }

    public boolean signup(SignupDto cred, ReturnDto response) {
        try {
            // Validate input
            if (cred.get_Email() == null || cred.get_Email().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty.");
            }
            if (cred.get_Password() == null || cred.get_Password().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }

            // Encrypt password before saving
            cred.set_Password(_PasswordEncoder.encode(cred.get_Password()));

            StringBuffer status = new StringBuffer();
            boolean isSuccess = _AuthRepository.signup(cred, status);

            if (!isSuccess) {
                // Repo failed, likely duplicate user or DB issue
                String err = status.toString().toLowerCase();
                if (err.contains("exists")) {
                    throw new UserAlreadyExistsException("User already exists: " + cred.get_Email());
                } else {
                    throw new DatabaseConnectionException("Database error: " + status);
                }
            }

            // Success
            response.set_Status("User registration successful.");
            response.set_Email(cred.get_Email());
            return true;
        } 
        catch (UserAlreadyExistsException | DatabaseConnectionException | IllegalArgumentException ex) {
            response.set_Email(cred.get_Email());
            response.set_Status("Signup failed: " + ex.getMessage());
            throw ex; // rethrow for controller to handle proper HTTP status
        } 
        catch (Exception ex) {
            response.set_Email(cred.get_Email());
            response.set_Status("Unexpected signup failure: " + ex.getMessage());
            throw new DatabaseConnectionException(ex.getMessage());
        }
    }

    public Boolean authenticate(AuthDto cred) {
        try {
            if (cred.get_Email() == null || cred.get_Email().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty.");
            }
            if (cred.get_Password() == null || cred.get_Password().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }

            StringBuffer status = new StringBuffer();
            StringBuffer passwordFromDB = new StringBuffer();

            Boolean isSuccess = _AuthRepository.getPasswordFromEmail(
                cred.get_Email(), passwordFromDB, status
            );

            if (!isSuccess) {
                throw new UserNotFoundException("No user found with email: " + cred.get_Email());
            }

            if (!_PasswordEncoder.matches(cred.get_Password(), passwordFromDB.toString())) {
                throw new IllegalArgumentException("Invalid password for " + cred.get_Email());
            }

            return true;
        } 
        catch (UserNotFoundException | IllegalArgumentException ex) {
            throw ex; // handled at controller
        } 
        catch (Exception ex) {
            throw new DatabaseConnectionException("Error fetching user: " + ex.getMessage());
        }
    }
}
