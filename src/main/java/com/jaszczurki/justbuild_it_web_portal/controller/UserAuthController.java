package com.jaszczurki.justbuild_it_web_portal.controller;

import com.jaszczurki.justbuild_it_web_portal.entity.User;
import com.jaszczurki.justbuild_it_web_portal.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        String username = user.getUsername();
        User existingUser = userAuthService.findUserByLogin(username);

        if(existingUser != null) {
            result.rejectValue("username", "error.user", "User already exists");
            return "registration";
        }

        String password = user.getPassword();

        if (username.equals("admin")) {
            userAuthService.addAdminFromForm(username, password);
        } else {
            userAuthService.addUserFromForm(username, password, false);
        }
        return "redirect:/login_form";
    }

    @GetMapping("/login_form")
    public String showLogin() {
        return "loginForm";
    }

    @PostMapping("/login_form")
    public String processLoginForm(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @GetMapping("/logout/logout_form")
    public String showLogout() {
        return "logoutForm";
    }
}
