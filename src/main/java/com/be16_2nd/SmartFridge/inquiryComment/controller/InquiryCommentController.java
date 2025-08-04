package com.be16_2nd.SmartFridge.inquiryComment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquirycomment")
public class InquiryCommentController {

    public ResponseEntity<?> create(){
        return null;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(){
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> myInquiryComment(){
        return null;
    }
}
