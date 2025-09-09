package com.be16_2nd.SmartFridge.post.repository;

import com.be16_2nd.SmartFridge.post.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
}
