package com.be16_2nd.SmartFridge.inquiry.service;

import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.fridge.repository.FridgeRepository;
import com.be16_2nd.SmartFridge.inquiry.dto.InquiryCreateDto;
import com.be16_2nd.SmartFridge.inquiry.domain.Inquiry;
import com.be16_2nd.SmartFridge.inquiry.repository.InquiryRepository;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.member.repository.MemberRepository;
import com.be16_2nd.SmartFridge.notification.domain.Notification;
import com.be16_2nd.SmartFridge.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final FridgeRepository fridgeRepository;

    public Long create(InquiryCreateDto inquiryCreateDto, Long fridgeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member sender = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 회원입니다."));
        Member receiver = memberRepository.findByEmail("admin@naver.com")
                .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 관리자입니다."));
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new EntityNotFoundException("등록되지 않은 냉장고입니다."));
        Inquiry inquiry = inquiryRepository.save(inquiryCreateDto.toEntity(sender, fridge));

        Notification notification = Notification.fromInquiry(sender, receiver, inquiry);
        notificationService.create(notification);
        return inquiry.getInquiryId();
    }
}
