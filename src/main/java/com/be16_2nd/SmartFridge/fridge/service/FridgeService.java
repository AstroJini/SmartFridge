package com.be16_2nd.SmartFridge.fridge.service;

import com.be16_2nd.SmartFridge.fridge.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeRepository fridgeRepository;

    public void create(){

    }
}
