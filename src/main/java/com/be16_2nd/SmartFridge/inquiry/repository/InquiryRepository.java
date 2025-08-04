package com.be16_2nd.SmartFridge.inquiry.repository;

import com.be16_2nd.SmartFridge.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
