package com.be16_2nd.SmartFridge.fridge.domain;

import com.be16_2nd.SmartFridge.common.domain.BaseTimeEntity;
import com.be16_2nd.SmartFridge.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
@Getter
public class FridgeMember extends BaseTimeEntity {



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private Type type;
}
