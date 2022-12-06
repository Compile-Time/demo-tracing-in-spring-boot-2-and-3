package com.example.springboot2server.repository;

import com.example.springboot2server.entity.User;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NewSpan("find user by name")
    Optional<User> findByName(@SpanTag("user.name") String userName);
}
