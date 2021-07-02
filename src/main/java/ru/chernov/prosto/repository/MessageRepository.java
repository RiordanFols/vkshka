package ru.chernov.prosto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.chernov.prosto.domain.entity.Message;

import java.util.Set;

/**
 * @author Pavel Chernov
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    Set<Message> findAllByAuthorIdAndTargetId(long authorId, long targetId);

    @Query(nativeQuery = true,
            value = "SELECT m.target_id, m.user_id FROM message m WHERE (m.target_id=?1 or m.user_id=?1);")
    Set<Long> findContactsIds(long userId);
}
