package com.be16_2nd.SmartFridge.food.controller;

import com.be16_2nd.SmartFridge.food.domain.Category;
import com.be16_2nd.SmartFridge.food.domain.StorageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAllEnums() {
        // Category Enum의 모든 값을 String 리스트로 변환
        List<String> categoryNames = Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // StorageType Enum의 모든 값을 String 리스트로 변환
        List<String> storageTypeNames = Arrays.stream(StorageType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Map 형태로 묶어서 반환
        Map<String, List<String>> enums = Map.of(
                "categories", categoryNames,
                "storageTypes", storageTypeNames
        );

        return ResponseEntity.ok(enums);
    }
}