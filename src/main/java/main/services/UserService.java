package main.services;

import main.model.MyUserPrincipal;
import main.model.User;
import main.repositories.UserRepository;
import main.web.dto.UserDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(UserDto userDto){
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        logger.info("User " + user + " saved in the database");
        return userRepository.save(user);
    }

    public User getUserByName(String name){
        User user = Optional.ofNullable(userRepository.findUserByEmail(name))
                .orElseThrow(()-> new RuntimeException("User with this name doesn't exist"));
        return user;
    }

    public User getUserFromSecurityContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) auth.getPrincipal();
        return getUserByName(principal.getUsername());
    }
}
