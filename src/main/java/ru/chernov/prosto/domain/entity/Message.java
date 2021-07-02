package ru.chernov.prosto.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Chernov
 */
@Entity
@Data
public class Message {

    public static final int MAX_IMAGES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(updatable = false, length = 5000, nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_id", updatable = false, nullable = false)
    private User target;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "message_imgs",
            joinColumns = @JoinColumn(name = "message_id", nullable = false, updatable = false))
    @Column(name = "img_filename", length = 100, nullable = false, updatable = false)
    private List<String> imgFilenames = new ArrayList<>(MAX_IMAGES);

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm")
    private LocalDateTime creationDateTime;
}
