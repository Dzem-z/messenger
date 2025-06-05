package com.project.messenger.repositories;

import com.project.messenger.entities.User;
import com.project.messenger.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    Optional<UserStatus> findByUser(User user);
}
