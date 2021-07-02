package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.chernov.prosto.domain.Role;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.formatter.UserInfoFormatter;
import ru.chernov.prosto.mail.MailInfo;
import ru.chernov.prosto.mail.MailManager;
import ru.chernov.prosto.page.Error;
import ru.chernov.prosto.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Pavel Chernov
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserInfoFormatter userInfoFormatter;
    private final MailManager mailManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserInfoFormatter userInfoFormatter,
                       MailManager mailManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userInfoFormatter = userInfoFormatter;
        this.mailManager = mailManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return user != null ? user : new User();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByActivationCode(String activationCode) {
        return userRepository.findByActivationCode(activationCode);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Error checkRegistrationData(String username, String email, String password, String passwordConfirm) {

        // если username уже существует
        if (loadUserByUsername(username) != null)
            return Error.USERNAME_IS_TAKEN;

        // если email уже привязан
        if (findByEmail(email) != null)
            return Error.EMAIL_IS_TAKEN;

        // если пароль слишком короткий (минимум 6 символов)
        if (password.length() < 6)
            return Error.TOO_SHORT_PASSWORD;

        // если пароль и подтверждение пароля не совпадают
        if (!password.equals(passwordConfirm))
            return Error.PASSWORDS_NOT_SAME;

        return null;
    }

    public void registration(String username, String gender, String email,
                             String name, String surname, String password) {
        User user = new User();
        user.setUsername(username);

        user.setGender(gender);

        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Role.USER));

        user.setActive(true);
//        user.setActive(false);
//        user.setActivationCode(UUID.randomUUID().toString());
        mailManager.send(new MailInfo(user, "1"));

        userRepository.save(user);
    }

    public void subscribe(long userId, long targetId) {
        User user = findById(userId);
        User target = findById(targetId);

        if (!user.getSubscriptions().contains(target)) {
            user.getSubscriptions().add(target);
            userRepository.save(user);
        }
    }

    public void unsubscribe(long userId, long targetId) {
        User user = findById(userId);
        User target = findById(targetId);

        if (user.getSubscriptions().contains(target)) {
            user.getSubscriptions().remove(target);
            userRepository.save(user);
        }
    }

    public boolean activateUser(String code) {
        User user = findByActivationCode(code);

        if (user == null)
            return false;

        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    public void updateLastOnline(long userId) {
        User user = findById(userId);
        user.setLastOnline(LocalDateTime.now());

        userRepository.save(user);
    }

    public void formatExtraInfo(User user) {
        userInfoFormatter.formatBirthdayString(user);
        userInfoFormatter.formatAge(user);
        userInfoFormatter.formatLastOnlineString(user);
    }
}
