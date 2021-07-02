package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.component.FileHandler;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.mail.MailInfo;
import ru.chernov.prosto.mail.MailManager;
import ru.chernov.prosto.page.Error;
import ru.chernov.prosto.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Pavel Chernov
 */
@Service
public class ProfileService {

    private final UserService userService;
    private final MailManager mailManager;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;

    @Autowired
    public ProfileService(UserService userService, MailManager mailManager,
                          PasswordEncoder passwordEncoder, FileHandler fileHandler) {
        this.userService = userService;
        this.mailManager = mailManager;
        this.passwordEncoder = passwordEncoder;
        this.fileHandler = fileHandler;
    }

    public User updateAvatar(long userId, MultipartFile avatar) throws IOException {

        User user = userService.findById(userId);

        if (ImageUtils.isImageTypeAllowed(avatar)) {

            // если у пользователя не стоковый аватар
            if (!user.getAvatarFilename().equals(user.getGender().getStockAvatarFilename()))
                // удаляем его старое фото
                fileHandler.deleteAvatar(user.getAvatarFilename());

            String filename = fileHandler.saveAvatar(avatar);
            user.setAvatarFilename(filename);
        }

        return userService.save(user);
    }

    public User deleteAvatar(long userId) throws IOException {

        User user = userService.findById(userId);

        // если у пользователя не стоковый аватар
        if (!user.getAvatarFilename().equals(user.getGender().getStockAvatarFilename())) {
            // удаляем его аватар
            fileHandler.deleteAvatar(user.getAvatarFilename());
            // ставим стоковое фото
            user.setAvatarFilename(user.getGender().getStockAvatarFilename());
        }

        return userService.save(user);
    }

    public Error updateData(long userId, String username, String gender, String name, String surname,
                           String status, String birthdayString, String email) {

        User user = userService.findById(userId);
        // если дата рождения юзера не пустая, то форматируем ее
        LocalDate birthday = null;
        if (StringUtils.hasLength(birthdayString)) {
            var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            birthday = LocalDate.parse(birthdayString, dtf);
        }

        // если юзернейм занят и при этом не принадлежит текущему юзеру
        if (userService.findByUsername(username) != null && !userService.findByUsername(username).equals(user))
            return Error.USERNAME_IS_TAKEN;

        // если почта занята и при этом не принадлежит текущему юзеру
        if (userService.findByEmail(email) != null && !userService.findByEmail(email).equals(user))
            return Error.EMAIL_IS_TAKEN;

        user.setUsername(username);
        user.setGender(gender);
        user.setName(name);
        user.setSurname(surname);
        user.setStatus(status);
        user.setBirthday(birthday);

        // если email был обновлен
        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
//            user.setActive(false);
//            user.setActivationCode(UUID.randomUUID().toString());
            mailManager.send(new MailInfo(user, "2"));
        }

        userService.save(user);
        return null;
    }

    public Error updatePassword(long userId, String oldPassword, String newPassword, String newPasswordConfirm) {
        User user = userService.findById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return Error.WRONG_PASSWORD;

        if (!newPassword.equals(newPasswordConfirm))
            return Error.PASSWORDS_NOT_SAME;

        if (newPassword.length() < 6)
            return Error.TOO_SHORT_NEW_PASSWORD;

        if (newPassword.equals(oldPassword))
            return Error.SAME_PASSWORDS;

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        mailManager.send(new MailInfo(user, "3"));
        return null;
    }
}
