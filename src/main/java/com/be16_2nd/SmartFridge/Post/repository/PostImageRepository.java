package com.be16_2nd.SmartFridge.Post.repository;

import com.be16_2nd.SmartFridge.Post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
