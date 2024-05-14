package dev.kvejp.taskmgr.controller;

import dev.kvejp.taskmgr.entity.UserDTO;
import dev.kvejp.taskmgr.repository.UserRepository;
import dev.kvejp.taskmgr.service.UserDataValidationService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {
    protected UserRepository userRepository;
    protected UserDataValidationService userDataValidationService;

    public RegisterController(UserRepository userService, UserDataValidationService userDataValidationService) {
        this.userRepository = userService;
        this.userDataValidationService = userDataValidationService;
    }
    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String register(@RequestParam("username") String name, @RequestParam("password") String password, @RequestParam("repeatpassword") String repeatpassword) {
        if (!password.equals(repeatpassword)) {
            return "redirect:/register?error=passwordsDontMatch";
        }

        String passwordError = userDataValidationService.isValid(name, password);
        if (!passwordError.isEmpty()) {
            return "redirect:/register?error=" + passwordError;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        UserDTO newUser = new UserDTO(name, hashedPassword);
        userRepository.save(newUser);

        return "redirect:/login";
    }
}
