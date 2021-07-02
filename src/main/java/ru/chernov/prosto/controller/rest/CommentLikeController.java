package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.chernov.prosto.domain.entity.Comment;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.CommentService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/comment-like")
public class CommentLikeController {

    private final CommentService commentService;

    @Autowired
    public CommentLikeController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{id}")
    public Map<String, Object> likesInfo(@AuthenticationPrincipal User user,
                                         @PathVariable(name = "id") long commentId) {

        var data = new HashMap<String, Object>();
        Comment comment = commentService.findById(commentId);
        data.put("likeN", comment.getLikes().size());
        data.put("isLiked", comment.getLikes().contains(user));

        return data;
    }

    @PostMapping("{id}")
    public void like(@AuthenticationPrincipal User user,
                     @PathVariable(name = "id") long commentId) {

        commentService.like(commentId, user.getId());

    }

    @DeleteMapping("{id}")
    public void unlike(@AuthenticationPrincipal User user,
                       @PathVariable(name = "id") long commentId) {

        commentService.unlike(commentId, user.getId());
    }
}
