package com.be16_2nd.SmartFridge.inquiryComment.repository;

import com.be16_2nd.SmartFridge.inquiryComment.domain.InquiryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryCommentRepository extends JpaRepository<InquiryComment, Long> {
}
