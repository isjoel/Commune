package com.commune.commune_backend.service;

import com.commune.commune_backend.model.Post;
import com.commune.commune_backend.repository.PostRepository;

public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post createPost(Post post){
        return postRepository.save(post);
    }

}
