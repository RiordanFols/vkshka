package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.chernov.prosto.domain.Gender;
import ru.chernov.prosto.domain.Role;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.mail.MailInfo;
import ru.chernov.prosto.mail.MailManager;
import ru.chernov.prosto.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Pavel Chernov
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MailManager mailManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       MailManager mailManager,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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

    public void registration(String username, String gender, String email,
                             String name, String surname, String password) {
        User user = new User();
        user.setUsername(username);
        this.setGender(user, gender);
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Role.USER));

        user.setActive(true);
//        user.setActive(false);
//        user.setActivationCode(UUID.randomUUID().toString());
        mailManager.send(new MailInfo(user, "1"));

        save(user);
    }

    public void subscribe(long userId, long targetId) {
        User user = findById(userId);
        User target = findById(targetId);

        if (!user.getSubscriptions().contains(target)) {
            user.getSubscriptions().add(target);
            save(user);
        }
    }

    public void unsubscribe(long userId, long targetId) {
        User user = findById(userId);
        User target = findById(targetId);

        if (user.getSubscriptions().contains(target)) {
            user.getSubscriptions().remove(target);
            save(user);
        }
    }

    public boolean activateUser(String code) {
        User user = findByActivationCode(code);

        if (user == null)
            return false;

        user.setActive(true);
        save(user);
        return true;
    }

    public void updateLastOnline(long userId) {
        User user = findById(userId);
        user.setLastOnline(LocalDateTime.now());

        save(user);
    }

    public void setGender(User user, String gender) {
        Gender newGender = Gender.valueOf(gender);
        // если пользователь еще не создан или имеет стоковый аватар
        if (user.getGender() == null || user.getAvatarFilename().equals(user.getGender().getStockAvatarFilename()))
            // ставим ему новый стоковый аватар
            user.setAvatarFilename(newGender.getStockAvatarFilename());

        user.setGender(newGender);
    }
}
