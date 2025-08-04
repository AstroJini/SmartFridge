package com.be16_2nd.SmartFridge.fridge.repository;

import com.be16_2nd.SmartFridge.fridge.domain.FridgeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeMemberRepository extends JpaRepository<FridgeMember, Long> {
}
