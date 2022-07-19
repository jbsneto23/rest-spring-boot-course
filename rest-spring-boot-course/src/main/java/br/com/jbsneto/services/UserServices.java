package br.com.jbsneto.services;

import br.com.jbsneto.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class UserServices implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Finding one user by username " + username);
        var user = repository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
           throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}
