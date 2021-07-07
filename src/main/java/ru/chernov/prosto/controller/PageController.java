package ru.chernov.prosto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.domain.Gender;
import ru.chernov.prosto.domain.entity.Post;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.MessageService;
import ru.chernov.prosto.service.PostService;
import ru.chernov.prosto.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Pavel Chernov
 */
@Controller
@RequestMapping("")
public class PageController {

    private final UserService userService;
    private final PostService postService;
    private final MessageService messageService;

    @Autowired
    public PageController(UserService userService, PostService postService, MessageService messageService) {
        this.userService = userService;
        this.postService = postService;
        this.messageService = messageService;
    }

    @GetMapping("/feed")
    public String feedPage(@AuthenticationPrincipal User user,
                           Model model) {
        Set<Post> feed = postService.getFeed(user.getId());
        Map<Object, Object> data = new HashMap<>();
        data.put("feed", feed);
        data.put("me", user);

        model.addAttribute("frontendData", data);
        return "main/feed";
    }

    @GetMapping("/user/{username}")
    public String userPage(@PathVariable String username,
                           @AuthenticationPrincipal User user,
                           Model model) {
        User foundUser = userService.findByUsername(username);
        if (foundUser == null)
            return "error/404";
        if (foundUser.equals(user))
            return "redirect:/me";

        Map<Object, Object> data = new HashMap<>();
        data.put("me", user);
        data.put("user", foundUser);
        data.put("userPosts", postService.getUserPosts(foundUser.getId()));

        model.addAttribute("frontendData", data);
        return "main/user";
    }

    @GetMapping("/me")
    public String myPage(@AuthenticationPrincipal User user,
                         Model model) {
        User me = userService.findById(user.getId());

        Map<Object, Object> data = new HashMap<>();
        data.put("me", me);
        data.put("userPosts", postService.getUserPosts(user.getId()));

        model.addAttribute("frontendData", data);
        return "main/me";
    }

    @PostMapping("/me")
    public String addPost(@AuthenticationPrincipal User user,
                          @RequestParam String text,
                          @RequestParam MultipartFile[] images) throws IOException {
        postService.create(user.getId(), text, images);

        return "redirect:/me";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User user,
                              @RequestParam(required = false) String error,
                              @RequestParam(required = false) String notification,
                              @RequestParam(required = false) String passwordError,
                              @RequestParam(required = false) String passwordNotification,
                              Model model) {
        Map<Object, Object> data = new HashMap<>();
        data.put("me", userService.findById(user.getId()));
        data.put("genders", Gender.getAll());
        data.put("error", error);
        data.put("notification", notification);
        data.put("passwordError", passwordError);
        data.put("passwordNotification", passwordNotification);

        model.addAttribute("frontendData", data);
        return "main/profile";
    }

    @GetMapping("/messenger")
    public String messenger(@AuthenticationPrincipal User user,
                            Model model) {

        Map<Object, Object> data = new HashMap<>();
        // get all users who user contacted with
        data.put("userList", messageService.getAllContacts(user.getId()));
        data.put("me", user);

        model.addAttribute("frontendData", data);
        return "main/messenger";
    }

    @GetMapping("/messenger/{username}")
    public String chat(@PathVariable String username,
                       @AuthenticationPrincipal User user,
                       Model model) {
        User target = userService.findByUsername(username);

        if (user.getUsername().equals(username) || target == null)
            return "error/404";

        Map<Object, Object> data = new HashMap<>();
        data.put("userList", messageService.getAllContacts(user.getId()));
        data.put("messages", messageService.getCorrespondence(target.getId(), user.getId()));
        data.put("me", user);
        data.put("target", target);

        model.addAttribute("frontendData", data);
        return "main/messenger";
    }

    @PostMapping("/messenger/{id}")
    public String addMessage(@AuthenticationPrincipal User user,
                             @PathVariable(name = "id") long targetId,
                             @RequestParam String text,
                             @RequestParam MultipartFile[] images) throws IOException {
        messageService.create(user.getId(), targetId, text, images);

        User target = userService.findById(targetId);
        return "redirect:/messenger/" + target.getUsername();
    }

    @GetMapping("/subscriptions")
    public String subscriptionsPage(@AuthenticationPrincipal User user,
                                    Model model) {

        Map<Object, Object> data = new HashMap<>();
        data.put("subscriptions", userService.findById(user.getId()).getSubscriptions());
        data.put("me", user);

        model.addAttribute("frontendData", data);
        return "main/subscriptions";
    }

    @GetMapping("/subscribers")
    public String subscribersPage(@AuthenticationPrincipal User user,
                                  Model model) {

        Map<Object, Object> data = new HashMap<>();
        data.put("subscribers", userService.findById(user.getId()).getSubscribers());
        data.put("me", user);

        model.addAttribute("frontendData", data);
        return "main/subscribers";
    }
}
