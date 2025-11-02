package com.ncu.banking.service;

import com.ncu.banking.model.User;
import com.ncu.banking.model.Role;
import com.ncu.banking.repository.UserRepository;
import com.ncu.banking.security.CustomUserDetails;
import com.ncu.banking.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetch user
        User user = userRepository.getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // fetch role (take the first role, if multiple)
        List<Role> roles = roleRepository.getRolesByUser(username);
        String roleName = roles.isEmpty() ? "CUSTOMER" : roles.get(0).getRole();

        return new CustomUserDetails(user, roleName);
    }
}
