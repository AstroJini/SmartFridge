package com.be16_2nd.SmartFridge.food.domain;

import com.be16_2nd.SmartFridge.food.dto.FoodUpdateDto;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private LocalDate storageStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDateTime;

    private String memo;
    private Boolean isShared;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Food updateFood(FoodUpdateDto foodUpdateDto) {
        LocalDateTime expirationDateTime = foodUpdateDto.getExpirationDate().atTime(foodUpdateDto.getExpirationHour(), foodUpdateDto.getExpirationMinute());
        this.name = foodUpdateDto.getName();
        this.quantity = foodUpdateDto.getQuantity();
        this.storageType = foodUpdateDto.getStorageType();
        this.category =  foodUpdateDto.getCategory();
        this.expirationDateTime = expirationDateTime;
        this.memo =  foodUpdateDto.getMemo();
        this.isShared = foodUpdateDto.getShareable();
        return this;
    }
}
