package com.project.ribbon.repository;

import com.project.ribbon.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface MemberRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String userid);

    Optional<User> findByUniqueKey(String uniqueKey);
}