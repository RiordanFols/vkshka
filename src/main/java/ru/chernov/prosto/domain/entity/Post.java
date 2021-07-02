package ru.chernov.prosto.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pavel Chernov
 */
@Entity
@Data
public class Post {

    public static final int MAX_IMAGES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(updatable = false, length = 5000, nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User author;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "post_imgs",
            joinColumns = @JoinColumn(name = "post_id", nullable = false, updatable = false))
    @Column(name = "img_filename", length = 100, nullable = false, updatable = false)
    private List<String> imgFilenames = new ArrayList<>(MAX_IMAGES);

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime creationDateTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "post_like",
            joinColumns = @JoinColumn(name = "post_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false))
    @JsonIgnore
    private Set<User> likes = new HashSet<>();
}
