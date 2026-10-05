package com.commune.commune_backend.repository;

import com.commune.commune_backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository  extends JpaRepository<Post, Integer> {

}
