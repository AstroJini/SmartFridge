package com.be16_2nd.SmartFridge.fridge.domain;

import com.be16_2nd.SmartFridge.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Fridge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fridgeId;
    @Column(nullable = false)
    private String fridgeName;
    private String description;

}

