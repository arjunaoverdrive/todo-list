package main.web.controllers;

import main.services.UserService;
import main.web.dto.UserDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final Logger logger = Logger.getLogger(RegistrationController.class);

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Model model){
        model.addAttribute("userDto", new UserDto());
        logger.info("return GET /registration page");
        return "registration";
    }

    @PostMapping("/signin")
    public String signin (@ModelAttribute("user") UserDto userDto){
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(userDto.getPassword());
            userDto.setPassword(encodedPassword);
            userService.saveUser(userDto);
            logger.info("User " + userDto + " sent to service layer");
        }catch (Exception e){
            logger.error("Cannot sign in with these credentials\n" + e.getMessage());
            return "redirect:/registration?error";
        }
        return "redirect:/registration?success";
    }
}
