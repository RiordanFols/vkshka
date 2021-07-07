package ru.chernov.prosto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.chernov.prosto.component.FormChecker;
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
public class AuthController {

    private final UserService userService;
    private final FormChecker formChecker;

    @Autowired
    public AuthController(UserService userService, FormChecker formChecker) {
        this.userService = userService;
        this.formChecker = formChecker;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String notification,
                            @RequestParam(required = false) String error,
                            Model model) {
        Map<Object, Object> data = new HashMap<>();
        // замена пустой ошибки на ошибку логина
        data.put("error", (error != null && error.isEmpty()) ? Error.WRONG_CREDENTIALS.toString() : error);
        data.put("notification", notification);
        model.addAttribute("frontendData", data);
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@RequestParam(required = false) String notification,
                                   @RequestParam(required = false) String error,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) String gender,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String surname,
                                   Model model) {

        Map<Object, Object> data = new HashMap<>();
        data.put("genders", Gender.getAll());
        data.put("notification", notification);
        data.put("error", error);
        var formData = new HashMap<>() {{
            put("username", username);
            put("gender", gender);
            put("email", email);
            put("name", name);
            put("surname", surname);
        }};
        data.put("formData", formData);

        model.addAttribute("frontendData", data);
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@RequestParam String username,
                               @RequestParam String gender,
                               @RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String password,
                               @RequestParam String passwordConfirm,
                               RedirectAttributes ra) {
        Error error = formChecker.checkRegistrationForm(username, email, password, passwordConfirm);
        if (error == null) {
            userService.registration(username, gender, email, name, surname, password);
            ra.addAttribute("notification", Notification.REGISTRATION_SUCCESSFUL.toString());
            return "redirect:/login";
        } else {
            ra.addAttribute("error", error.toString());
            ra.addAttribute("username", username);
            ra.addAttribute("gender", gender);
            ra.addAttribute("name", name);
            ra.addAttribute("surname", surname);
            ra.addAttribute("email", email);
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
