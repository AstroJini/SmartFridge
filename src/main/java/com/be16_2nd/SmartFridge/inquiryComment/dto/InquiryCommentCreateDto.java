package com.be16_2nd.SmartFridge.inquiryComment.dto;

import com.be16_2nd.SmartFridge.inquiry.domain.Inquiry;
import com.be16_2nd.SmartFridge.inquiryComment.domain.InquiryComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryCommentCreateDto {
    private String content;

    public InquiryComment toEntity(InquiryCommentCreateDto inquiryCommentCreateDto, Inquiry inquiry) {
        return InquiryComment.builder()
                .commentContents(inquiryCommentCreateDto.getContent())
                .inquiry(inquiry).build();
    }

}
