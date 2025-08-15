package com.be16_2nd.SmartFridge.chat.repository;

import com.be16_2nd.SmartFridge.chat.domain.IsRead;
import com.be16_2nd.SmartFridge.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IsReadRepository extends JpaRepository<IsRead, Long> {
    List<IsRead> findAllByRoomIdAndMember(Long roomId, Member member);
}
