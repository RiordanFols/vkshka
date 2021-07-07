package ru.chernov.prosto.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.page.Error;
import ru.chernov.prosto.service.UserService;
import ru.chernov.prosto.utils.ImageUtils;

/**
 * @author Pavel Chernov
 */
@Component
public class FormChecker {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FormChecker(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public Error checkRegistrationForm(String username, String email, String password, String passwordConfirm) {

        // если username уже существует
        if (!userService.loadUserByUsername(username).equals(new User()))
            return Error.USERNAME_IS_TAKEN;

        // если email уже привязан
        if (userService.findByEmail(email) != null)
            return Error.EMAIL_IS_TAKEN;

        // если пароль слишком короткий (минимум 6 символов)
        if (password.length() < 6)
            return Error.TOO_SHORT_PASSWORD;

        // если пароль и подтверждение пароля не совпадают
        if (!password.equals(passwordConfirm))
            return Error.PASSWORDS_NOT_SAME;

        return null;
    }

    public Error checkProfileUpdateForm(long userId, String username, String email) {
        User user = userService.findById(userId);

        // если юзернейм занят и при этом не принадлежит текущему юзеру
        if (userService.findByUsername(username) != null && !userService.findByUsername(username).equals(user))
            return Error.USERNAME_IS_TAKEN;

        // если почта занята и при этом не принадлежит текущему юзеру
        if (userService.findByEmail(email) != null && !userService.findByEmail(email).equals(user))
            return Error.EMAIL_IS_TAKEN;

        return null;
    }

    public Error checkPasswordUpdateForm(long userId, String oldPassword,
                                         String newPassword, String newPasswordConfirm) {
        User user = userService.findById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return Error.WRONG_PASSWORD;

        if (!newPassword.equals(newPasswordConfirm))
            return Error.PASSWORDS_NOT_SAME;

        if (newPassword.length() < 6)
            return Error.TOO_SHORT_NEW_PASSWORD;

        if (newPassword.equals(oldPassword))
            return Error.SAME_PASSWORDS;

        return null;
    }

    public Error checkUploadedImage(MultipartFile image) {
        return ImageUtils.isImageTypeAllowed(image) ? null : Error.BAD_IMAGE_FORMAT;
    }

    public Error checkUploadedImages(MultipartFile[] images) {
        for (var image : images) {
            if (!ImageUtils.isImageTypeAllowed(image))
                return Error.BAD_IMAGE_FORMAT;
        }
        return null;
    }
}
