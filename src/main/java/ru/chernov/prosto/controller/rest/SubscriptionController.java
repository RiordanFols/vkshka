package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final UserService userService;

    @Autowired
    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public Map<String, Object> subscriptionsInfo(@AuthenticationPrincipal User user,
                                                 @PathVariable(name = "id") long targetId) {
        Map<String, Object> data = new HashMap<>();
        User target = userService.findById(targetId);
        data.put("subscriptionsN", target.getSubscriptions().size());
        data.put("subscribersN", target.getSubscribers().size());
        data.put("isSubscribed", target.getSubscribers().contains(user));

        return data;
    }

    @PostMapping("{id}")
    public void subscribe(@AuthenticationPrincipal User user,
                          @PathVariable(name = "id") long targetId) {
        userService.subscribe(user.getId(), targetId);
    }

    @DeleteMapping("{id}")
    public void unsubscribe(@AuthenticationPrincipal User user,
                            @PathVariable(name = "id") long targetId) {
        userService.unsubscribe(user.getId(), targetId);
    }
}
