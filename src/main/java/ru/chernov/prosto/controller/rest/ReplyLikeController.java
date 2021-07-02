package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.chernov.prosto.domain.entity.Reply;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("/reply-like")
public class ReplyLikeController {

    private final ReplyService replyService;

    @Autowired
    public ReplyLikeController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping("{id}")
    public Map<String, Object> likesInfo(@AuthenticationPrincipal User user,
                                         @PathVariable(name = "id") long replyId) {

        var data = new HashMap<String, Object>();
        Reply reply = replyService.findById(replyId);
        data.put("likeN", reply.getLikes().size());
        data.put("isLiked", reply.getLikes().contains(user));

        return data;
    }

    @PostMapping("{id}")
    public void like(@AuthenticationPrincipal User user,
                     @PathVariable(name = "id") long replyId) {
        replyService.like(replyId, user.getId());

    }

    @DeleteMapping("{id}")
    public void unlike(@AuthenticationPrincipal User user,
                       @PathVariable(name = "id") long replyId) {
        replyService.unlike(replyId, user.getId());
    }
}
