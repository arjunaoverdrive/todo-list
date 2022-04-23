package main.services;

import main.model.MyUserPrincipal;
import main.model.User;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = Optional.ofNullable(userRepository.findUserByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Set<GrantedAuthority> authorities = new HashSet<>();
        return new MyUserPrincipal(user, authorities);
    }
}
