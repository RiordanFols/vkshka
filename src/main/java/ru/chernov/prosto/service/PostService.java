package ru.chernov.prosto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.chernov.prosto.component.FileHandler;
import ru.chernov.prosto.domain.entity.Post;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.repository.PostRepository;
import ru.chernov.prosto.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Pavel Chernov
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileHandler fileHandler;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, FileHandler fileHandler) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.fileHandler = fileHandler;
    }

    public List<Post> getUserPosts(long authorId) {
        return postRepository.findAllByAuthorIdOrderByCreationDateTimeDesc(authorId);
    }

    public Set<Post> getFeed(long userId) {
        User user = userService.findById(userId);
        Set<Post> feed = new TreeSet<>(Comparator.comparing(Post::getCreationDateTime).reversed());
        for (var subscription : user.getSubscriptions()) {
            feed.addAll(getUserPosts(subscription.getId()));
        }
        return feed;
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post create(long userId, String text, MultipartFile[] images) throws IOException {
        Post post = new Post();
        post.setAuthor(userService.findById(userId));
        post.setText(text);
        post.setCreationDateTime(LocalDateTime.now());

        for (var image: images) {
            if (ImageUtils.isImageTypeAllowed(image) && post.getImgFilenames().size() < Post.MAX_IMAGES) {
                String filename = fileHandler.saveImage(image);
                post.getImgFilenames().add(filename);
            }
        }

        return postRepository.save(post);
    }

    public void delete(long postId) throws IOException {
        Post post = findById(postId);
        for (var filename: post.getImgFilenames())
            fileHandler.deleteImage(filename);

        postRepository.deleteById(postId);
    }

    public void likePost(long postId, long userId) {
        Post post = findById(postId);
        User user = userService.findById(userId);

        if (!post.getLikes().contains(user)) {
            post.getLikes().add(user);
            postRepository.save(post);
        }
    }

    public void unlikePost(long postId, long userId) {
        Post post = findById(postId);
        User user = userService.findById(userId);

        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
            postRepository.save(post);
        }
    }
}
