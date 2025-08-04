package com.be16_2nd.SmartFridge.food.controller;

import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import com.be16_2nd.SmartFridge.food.domain.Food;
import com.be16_2nd.SmartFridge.food.dto.FoodCreateDto;
import com.be16_2nd.SmartFridge.food.dto.FoodResDto;
import com.be16_2nd.SmartFridge.food.dto.FoodSearchDto;
import com.be16_2nd.SmartFridge.food.dto.FoodUpdateDto;
import com.be16_2nd.SmartFridge.food.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/register")
    public ResponseEntity<?> registerFood(@RequestBody @Valid FoodCreateDto foodCreateDto) {
        Food food = foodService.registerFood(foodCreateDto);
        return new  ResponseEntity<>(
                CommonDto.builder()
                        .result(food)
                        .status_code(HttpStatus.CREATED.value())
                        .status_message("상품등록완료")
                        .build()
                , HttpStatus.CREATED);
    }

    @GetMapping("/list/shared")
    public ResponseEntity<?> SharedFoodsList(Pageable pageable, @ModelAttribute FoodSearchDto foodSearchDto) {
        Page<FoodResDto> foodResDtoList = foodService.findAll(pageable,foodSearchDto, true);
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(foodResDtoList)
                        .status_code(HttpStatus.OK.value())
                        .status_message("공유 식품 목록 조회 완료")
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/list/nonshared")
    public ResponseEntity<?> NonSharedFoodsList(Pageable pageable, @ModelAttribute FoodSearchDto foodSearchDto) {
        Page<FoodResDto> foodResDtoList = foodService.findAll(pageable,foodSearchDto, false);
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(foodResDtoList)
                        .status_code(HttpStatus.OK.value())
                        .status_message("비공유 식품 목록 조회 완료")
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/my/shared")
    public ResponseEntity<?> SharedMyFoods(Pageable pageable, @ModelAttribute FoodSearchDto foodSearchDto) {
        Page<FoodResDto> foodResDtoList = foodService.findMyFoods(pageable, foodSearchDto, true);
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(foodResDtoList)
                        .status_code(HttpStatus.OK.value())
                        .status_message("내 공유 식품 목록 조회 완료")
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/my/nonshared")
    public ResponseEntity<?> NonSharedMyFoods(Pageable pageable, @ModelAttribute FoodSearchDto foodSearchDto) {
        Page<FoodResDto> foodResDtoList = foodService.findMyFoods(pageable, foodSearchDto, false);
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(foodResDtoList)
                        .status_code(HttpStatus.OK.value())
                        .status_message("내 비공유 식품 목록 조회 완료")
                        .build(),
                HttpStatus.OK);
    }

    @PutMapping("/update/{foodId}")
    public ResponseEntity<?> updateFood(@PathVariable Long foodId, @RequestBody @Valid FoodUpdateDto foodUpdateDto) {
        Food food = foodService.updateFood(foodUpdateDto, foodId);
        return new  ResponseEntity<>(
                CommonDto.builder()
                        .result(food)
                        .status_code(HttpStatus.OK.value())
                        .status_message("식품정보수정완료")
                        .build()
                , HttpStatus.OK);
    }

}
