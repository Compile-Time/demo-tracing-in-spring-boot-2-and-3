package com.example.springboot3server.repository;

import com.example.springboot3server.entity.User;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NewSpan("find user by name")
    Optional<User> findByName(@SpanTag("user.name") String userName);
}
