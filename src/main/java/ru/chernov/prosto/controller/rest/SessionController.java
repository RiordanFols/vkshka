package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.UserService;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;

    @Autowired
    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public void updateLastOnline(@AuthenticationPrincipal User user) {
        assert user != null;
        userService.updateLastOnline(user.getId());
    }
}
