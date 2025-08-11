package com.be16_2nd.SmartFridge.inquiry.controller;

import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import com.be16_2nd.SmartFridge.inquiry.InquiryCreateDto;
import com.be16_2nd.SmartFridge.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    public ResponseEntity<?> create(@RequestBody InquiryCreateDto inquiryCreateDto, Long fridgeId){
        return new ResponseEntity<>(CommonDto.builder()
                .result(inquiryService.create(inquiryCreateDto, fridgeId))
                .status_code(HttpStatus.CREATED.value())
                .status_message("문의 등록 완료").build(), HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(){
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> myInquiry(){
        return null;
    }
}
