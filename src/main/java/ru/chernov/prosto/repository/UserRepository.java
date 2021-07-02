package ru.chernov.prosto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernov.prosto.domain.entity.User;

/**
 * @author Pavel Chernov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByActivationCode(String activationCode);
}
