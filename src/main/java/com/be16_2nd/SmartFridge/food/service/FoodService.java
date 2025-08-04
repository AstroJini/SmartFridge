package com.be16_2nd.SmartFridge.food.service;

import com.be16_2nd.SmartFridge.food.domain.Food;
import com.be16_2nd.SmartFridge.food.dto.FoodCreateDto;
import com.be16_2nd.SmartFridge.food.dto.FoodResDto;
import com.be16_2nd.SmartFridge.food.dto.FoodSearchDto;
import com.be16_2nd.SmartFridge.food.dto.FoodUpdateDto;
import com.be16_2nd.SmartFridge.food.repository.FoodRepository;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    public final MemberRepository memberRepository;
    private final FoodRepository foodRepository;
    public Food registerFood(FoodCreateDto foodCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email  = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는사용자 입니다"));
        Food food = foodCreateDto.toEntity(member);
        return foodRepository.save(food);
    }

    public Page<FoodResDto> findAll(Pageable pageable, FoodSearchDto foodSearchDto, boolean isShared) {

        Specification<Food> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                // 공유 여부 필수 조건
                predicateList.add(criteriaBuilder.equal(root.get("isShared"), isShared));

                // 카테고리 조건
                if (foodSearchDto.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), foodSearchDto.getCategory()));
                }

                // 식품명 검색 조건
                if (foodSearchDto.getFoodName() != null && !foodSearchDto.getFoodName().isBlank()) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%" + foodSearchDto.getFoodName() + "%"));
                }

                // 리스트를 배열로 변환
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }

                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Food> foodList = foodRepository.findAll(specification, pageable);
        return foodList.map(FoodResDto::fromEntity);
    }

    public Page<FoodResDto> findMyFoods(Pageable pageable, FoodSearchDto foodSearchDto, boolean isShared) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));

        Specification<Food> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                // 사용자 본인의 식품만
                predicateList.add(criteriaBuilder.equal(root.get("member"), member));

                // 공유 여부 조건
                predicateList.add(criteriaBuilder.equal(root.get("isShared"), isShared));

                // 카테고리 조건
                if (foodSearchDto.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), foodSearchDto.getCategory()));
                }

                // 식품명 검색 조건
                if (foodSearchDto.getFoodName() != null && !foodSearchDto.getFoodName().isBlank()) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%" + foodSearchDto.getFoodName() + "%"));
                }

                // 리스트를 배열로 변환하여 and로 묶기
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }

                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Food> foodList = foodRepository.findAll(specification, pageable);
        return foodList.map(FoodResDto::fromEntity);
    }

    public Food updateFood(FoodUpdateDto foodUpdateDto, Long id) {
        Food food =  foodRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 식품입니다"));
        return food.updateFood(foodUpdateDto);
    }

}
