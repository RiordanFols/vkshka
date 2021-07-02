package ru.chernov.prosto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.chernov.prosto.domain.Gender;
import ru.chernov.prosto.page.Error;
import ru.chernov.prosto.page.Notification;
import ru.chernov.prosto.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String notification,
                            @RequestParam(required = false) String error,
                            Model model) {
        Map<Object, Object> data = new HashMap<>();
        data.put("notification", notification);
        if (error != null && error.isEmpty()) {
            data.put("error", Error.WRONG_CREDENTIALS.toString());
        } else {
            data.put("error", error);
        }


        model.addAttribute("frontendData", data);
        return "guest/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@RequestParam(required = false) String notification,
                                   @RequestParam(required = false) String error,
                                   Model model) {

        Map<Object, Object> data = new HashMap<>();
        data.put("genders", Gender.getAll());
        data.put("notification", notification);
        data.put("error", error);

        model.addAttribute("frontendData", data);
        return "guest/registration";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam String username,
                               @RequestParam String gender,
                               @RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String password,
                               @RequestParam String passwordConfirm,
                               RedirectAttributes redirectAttributes) {
        Error error = userService.checkRegistrationData(username, email, password, passwordConfirm);
        if (error == null) {
            userService.registration(username, gender, email, name, surname, password);
            redirectAttributes.addAttribute("notification", Notification.REGISTRATION_SUCCESSFUL.toString());
            return "redirect:/login";
        } else {
            redirectAttributes.addAttribute("error", error.toString());
            return "redirect:/registration";
        }
    }

    @GetMapping("/activation{code}")
    public String activation(@PathVariable String code) {
        if (userService.activateUser(code)) {
            return "redirect:/login";
        } else {
            return "error/404";
        }
    }
}
