package com.igor_klimov.main.web.controllers;

import com.igor_klimov.main.services.LoginService;
import com.igor_klimov.main.web.dto.LoginForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = Logger.getLogger(LoginController.class);

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        logger.info("authentication authorities "  + authentication.getAuthorities());
        GrantedAuthority authority = () -> "[ROLE_ANONYMOUS]";
//        logger.info("granted authority " + authority.getAuthority());
        if (!authentication.getAuthorities().toString().equals(authority.getAuthority())) {
            SecurityContextHolder.clearContext();
            authentication.setAuthenticated(false);
            logger.info("security context was cleared");
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping
    public String authenticate(LoginForm loginForm) {
        if (loginService.authenticate(loginForm)) {
            logger.info("Login success. Redirect to index.html");
            return "redirect:/";
        } else {
            logger.info("Login failure. Redirect to login page");
            return "redirect:/login?error";
        }
    }
}
