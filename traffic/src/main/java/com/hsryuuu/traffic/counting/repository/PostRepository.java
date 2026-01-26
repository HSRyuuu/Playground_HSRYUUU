package com.hsryuuu.traffic.counting.repository;

import com.hsryuuu.traffic.counting.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
