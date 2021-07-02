package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.component.FileHandler;
import ru.chernov.prosto.domain.entity.Comment;
import ru.chernov.prosto.domain.entity.Post;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.repository.CommentRepository;
import ru.chernov.prosto.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Pavel Chernov
 */
@Service
public class CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final FileHandler fileHandler;

    @Autowired
    public CommentService(UserService userService, CommentRepository commentRepository,
                          PostService postService, FileHandler fileHandler) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.fileHandler = fileHandler;
    }

    public Comment findById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment create(long userId, long postId, String text, MultipartFile[] images) throws IOException {
        User author = userService.findById(userId);
        Post post = postService.findById(postId);

        Comment comment = new Comment();
        comment.setCreationDateTime(LocalDateTime.now());
        comment.setText(text);
        comment.setAuthor(author);
        comment.setPost(post);

        for (var image: images) {
            if (ImageUtils.isImageTypeAllowed(image) && comment.getImgFilenames().size() < Comment.MAX_IMAGES) {
                String filename = fileHandler.saveImage(image);
                comment.getImgFilenames().add(filename);
            }
        }

        return commentRepository.save(comment);
    }

    public void delete(long commentId) throws IOException {
        Comment comment = findById(commentId);
        for (var filename: comment.getImgFilenames())
            fileHandler.deleteImage(filename);

        commentRepository.deleteById(commentId);
    }

    public Set<Comment> getPostComments(long postId) {
        Set<Comment> comments = new TreeSet<>(Comparator
                .comparingInt((Comment c) -> c.getLikes().size())
                .reversed()
                .thenComparing(Comment::getCreationDateTime));
        comments.addAll(commentRepository.findAllByPostId(postId));
        return comments;
    }

    public void like(long commentId, long userId) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);

        if (!comment.getLikes().contains(user)) {
            comment.getLikes().add(user);
            commentRepository.save(comment);
        }
    }

    public void unlike(long commentId, long userId) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);

        if (comment.getLikes().contains(user)) {
            comment.getLikes().remove(user);
            commentRepository.save(comment);
        }
    }
}
