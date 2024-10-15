package com.bgaebalja.blogbackend.post.repository;

import com.bgaebalja.blogbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndDeleteYnFalseAndCompleteYnFalse(Long id);

    Optional<Post> findByIdAndDeleteYnFalseAndCompleteYnTrue(Long id);
}