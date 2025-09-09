package com.be16_2nd.SmartFridge.post.repository;

import com.be16_2nd.SmartFridge.post.domain.Post;
import com.be16_2nd.SmartFridge.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Transactional
    @Modifying
    void deleteByPostAndImageUrl(Post post, String imageUrl);
}
