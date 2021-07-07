package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.formatter.UserInfoFormatter;
import ru.chernov.prosto.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    private final UserInfoFormatter userInfoFormatter;
    private final UserService userService;

    @Autowired
    public UserInfoController(UserInfoFormatter userInfoFormatter, UserService userService) {
        this.userInfoFormatter = userInfoFormatter;
        this.userService = userService;
    }

    @GetMapping("{id}")
    public Map<String, String> getExtraUserInfo(@PathVariable(name = "id") long userId) {
        User user = userService.findById(userId);
        return new HashMap<>() {{
            put("age", userInfoFormatter.formatAge(user));
            put("birthday", userInfoFormatter.formatBirthdayString(user));
            put("lastOnline", userInfoFormatter.formatLastOnlineString(user));
        }};
    }
}
