package com.example.jpa.user.repository;

import com.example.jpa.user.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLonginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
}
