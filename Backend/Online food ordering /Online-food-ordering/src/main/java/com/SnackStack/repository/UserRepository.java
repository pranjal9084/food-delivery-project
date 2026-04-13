package com.SnackStack.repository;

import com.SnackStack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String UserName);
}
