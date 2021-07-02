package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.component.FileHandler;
import ru.chernov.prosto.domain.entity.Comment;
import ru.chernov.prosto.domain.entity.Reply;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.repository.ReplyRepository;
import ru.chernov.prosto.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Pavel Chernov
 */
@Service
public class ReplyService {

    private final UserService userService;
    private final ReplyRepository replyRepository;
    private final CommentService commentService;
    private final FileHandler fileHandler;

    @Autowired
    public ReplyService(UserService userService, ReplyRepository replyRepository,
                        CommentService commentService, FileHandler fileHandler) {
        this.userService = userService;
        this.replyRepository = replyRepository;
        this.commentService = commentService;
        this.fileHandler = fileHandler;
    }

    public Reply findById(long replyId) {
        return replyRepository.findById(replyId).orElse(null);
    }


    public Reply create(long userId, long commentId, String text, MultipartFile[] images) throws IOException {
        User author = userService.findById(userId);
        Comment comment = commentService.findById(commentId);

        Reply reply = new Reply();
        reply.setCreationDateTime(LocalDateTime.now());
        reply.setAuthor(author);
        reply.setComment(comment);
        reply.setText(text);

        for (var image: images) {
            if (ImageUtils.isImageTypeAllowed(image) && reply.getImgFilenames().size() < Reply.MAX_IMAGES) {
                String filename = fileHandler.saveImage(image);
                reply.getImgFilenames().add(filename);
            }
        }

        return replyRepository.save(reply);
    }

    public void delete(long replyId) throws IOException {
        Reply reply = findById(replyId);
        for (var filename: reply.getImgFilenames())
            fileHandler.deleteImage(filename);

        replyRepository.deleteById(replyId);
    }

    public Set<Reply> findRepliesByCommentId(long commentId) {
        return replyRepository.findAllByCommentIdOrderByCreationDateTime(commentId);
    }

    public void like(long replyId, long userId) {
        User user = userService.findById(userId);
        Reply reply = findById(replyId);

        if (!reply.getLikes().contains(user)) {
            reply.getLikes().add(user);
            replyRepository.save(reply);
        }
    }

    public void unlike(long replyId, long userId) {
        User user = userService.findById(userId);
        Reply reply = findById(replyId);

        if (reply.getLikes().contains(user)) {
            reply.getLikes().remove(user);
            replyRepository.save(reply);
        }
    }
}
