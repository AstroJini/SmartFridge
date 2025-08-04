package com.be16_2nd.SmartFridge.fridge.controller;

import com.be16_2nd.SmartFridge.fridge.dto.FridgeCreateDto;
import com.be16_2nd.SmartFridge.fridge.service.FridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fridge")
public class FridgeController {

    private final FridgeService fridgeService;

    @PostMapping("/create")
    public ResponseEntity<?> create(FridgeCreateDto fridgeCreateDto){
        return null;
    }
}
