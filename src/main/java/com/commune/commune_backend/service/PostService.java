package com.commune.commune_backend.service;

import com.commune.commune_backend.dto.PostResponse;
import com.commune.commune_backend.model.Post;
import com.commune.commune_backend.model.User;
import com.commune.commune_backend.repository.PostRepository;
import com.commune.commune_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostResponse createPost(Post post) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setUser(user);
        post.setId(null);
        post.setCreatedAt(null);

        Post savedPost = postRepository.save(post);

        return new PostResponse(
                savedPost.getId(),
                savedPost.getContent(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                savedPost.getCreatedAt()
        );
    }

    public java.util.List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getUser().getUsername(),
                        post.getUser().getFirstName(),
                        post.getUser().getLastName(),
                        post.getCreatedAt()
                ))
                .toList();
    }
}
