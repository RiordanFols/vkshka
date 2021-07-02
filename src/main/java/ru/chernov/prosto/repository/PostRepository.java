package ru.chernov.prosto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chernov.prosto.domain.entity.Post;

import java.util.List;

/**
 * @author Pavel Chernov
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorIdOrderByCreationDateTimeDesc(long id);
}
