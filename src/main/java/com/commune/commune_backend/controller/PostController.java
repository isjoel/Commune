package com.commune.commune_backend.controller;

import com.commune.commune_backend.dto.PostResponse;
import com.commune.commune_backend.model.Post;
import com.commune.commune_backend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public PostResponse createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }
}
