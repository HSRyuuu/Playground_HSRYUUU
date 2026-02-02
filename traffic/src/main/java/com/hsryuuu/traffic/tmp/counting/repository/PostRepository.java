package com.hsryuuu.traffic.tmp.counting.repository;

import com.hsryuuu.traffic.tmp.counting.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
