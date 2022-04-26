package main.services;

import main.web.dto.LoginForm;
import main.model.User;
import main.repositories.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger(LoginService.class);

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(LoginForm loginForm) {
        User user = userRepository.findUserByEmail(loginForm.getUsername());
        if (user == null) {
            logger.info("User with email " + loginForm.getUsername() + " not found");
            return false;
        }
        logger.info("User " + loginForm.getUsername() + " exists in the database. Checking password");
        if (user.getPassword().equals(loginForm.getPassword())) {
            logger.info("User " + user + " retrieved from the database");
            return true;
        } else {
            logger.info("User credentials are wrong");
            return false;
        }

        //todo implement password reset
    }

}
