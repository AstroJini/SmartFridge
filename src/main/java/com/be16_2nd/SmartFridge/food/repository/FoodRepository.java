package com.be16_2nd.SmartFridge.food.repository;

import com.be16_2nd.SmartFridge.food.domain.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findAll(Specification<Food> specification, Pageable pageable);
}
