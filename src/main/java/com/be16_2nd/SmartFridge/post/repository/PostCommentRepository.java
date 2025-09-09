package com.be16_2nd.SmartFridge.post.repository;

import com.be16_2nd.SmartFridge.post.domain.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Page<PostComment> findByPostId(Long postId, Pageable pageable);

    Long countByMemberId(UUID memberId);
}
