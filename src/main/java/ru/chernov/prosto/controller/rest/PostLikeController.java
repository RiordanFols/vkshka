package ru.chernov.prosto.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.chernov.prosto.domain.entity.Post;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.service.PostService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Chernov
 */
@RestController
@RequestMapping("post-like")
public class PostLikeController {

    private final PostService postService;

    @Autowired
    public PostLikeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("{id}")
    public Map<String, Object> likesInfo(@AuthenticationPrincipal User user,
                                         @PathVariable(name = "id") long postId) {

        var data = new HashMap<String, Object>();
        Post post = postService.findById(postId);
        data.put("likeN", post.getLikes().size());
        data.put("isLiked", post.getLikes().contains(user));

        return data;
    }

    @PostMapping("{id}")
    public void like(@AuthenticationPrincipal User user,
                     @PathVariable(name = "id") long postId) {

        postService.likePost(postId, user.getId());

    }

    @DeleteMapping("{id}")
    public void unlike(@AuthenticationPrincipal User user,
                       @PathVariable(name = "id") long postId) {

        postService.unlikePost(postId, user.getId());
    }
}
