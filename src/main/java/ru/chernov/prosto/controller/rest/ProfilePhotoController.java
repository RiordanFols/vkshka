package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.ProfileService;

import java.io.IOException;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("profile/photo")
public class ProfilePhotoController {

    private final ProfileService profileService;

    @Autowired
    public ProfilePhotoController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public User updateProfilePhoto(@AuthenticationPrincipal User user,
                                   @RequestParam("avatar") MultipartFile avatar) throws IOException {
        return profileService.updateAvatar(user.getId(), avatar);
    }

    @DeleteMapping
    public User deleteAvatar(@AuthenticationPrincipal User user) throws IOException {
        return profileService.deleteAvatar(user.getId());
    }
}
