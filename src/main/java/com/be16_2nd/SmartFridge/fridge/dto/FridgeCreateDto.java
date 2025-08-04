package com.be16_2nd.SmartFridge.fridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FridgeCreateDto {
    private String FridgeName;
    private String description;
}
