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

            // ставим новое фото
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

    public void updateData(long userId, String username, String gender, String name, String surname,
                           String status, String birthdayString, String email) {
        User user = userService.findById(userId);
        // если дата рождения юзера не пустая, то форматируем ее
        LocalDate birthday = null;
        if (StringUtils.hasLength(birthdayString)) {
            var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            birthday = LocalDate.parse(birthdayString, dtf);
        }

        user.setUsername(username);
        userService.setGender(user, gender);
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
    }

    public void updatePassword(long userId, String newPassword) {
        User user = userService.findById(userId);

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        mailManager.send(new MailInfo(user, "3"));
    }
}
