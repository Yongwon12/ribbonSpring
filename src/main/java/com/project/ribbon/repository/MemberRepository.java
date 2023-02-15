package com.project.ribbon.repository;

import com.project.ribbon.dto.Member;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);

}