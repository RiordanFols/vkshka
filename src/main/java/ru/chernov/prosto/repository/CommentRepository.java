package ru.chernov.prosto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernov.prosto.domain.entity.Comment;

import java.util.Set;

/**
 * @author Pavel Chernov
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByPostId(long id);
}
