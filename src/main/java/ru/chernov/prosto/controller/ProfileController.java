package ru.chernov.prosto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.page.Error;
import ru.chernov.prosto.page.Notification;
import ru.chernov.prosto.service.ProfileService;

import java.io.IOException;

/**
 * @author Pavel Chernov
 */
@Controller
@RequestMapping("profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/update/avatar")
    public String updateProfilePhoto(@AuthenticationPrincipal User user,
                                     @RequestParam("avatar") MultipartFile avatar) throws IOException {
        profileService.updateAvatar(user.getId(), avatar);

        return "redirect:/profile";
    }

    @PostMapping("/update/data")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String username,
                                @RequestParam String gender,
                                @RequestParam String name,
                                @RequestParam String surname,
                                @RequestParam String status,
                                @RequestParam(name = "birthday") String birthdayString,
                                @RequestParam String email,
                                RedirectAttributes redirectAttributes) {

        Error error = profileService.updateData(
                user.getId(), username, gender, name, surname, status, birthdayString, email);

        if (error == null) {
            redirectAttributes.addAttribute("notification", Notification.DATA_UPDATE_SUCCESSFUL.toString());
        } else {
            redirectAttributes.addAttribute("error", error.toString());
        }

        return "redirect:/profile";
    }

    @PostMapping("/update/password")
    public String updatePassword(@AuthenticationPrincipal User user,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String newPasswordConfirm,
                                 RedirectAttributes redirectAttributes) {
        Error error = profileService.updatePassword(user.getId(), oldPassword, newPassword, newPasswordConfirm);
        if (error == null) {
            redirectAttributes.addAttribute("passwordNotification", Notification.PASSWORD_UPDATE_SUCCESSFUL.toString());
        } else {
            redirectAttributes.addAttribute("passwordError", error.toString());
        }
        return "redirect:/profile";
    }
}
