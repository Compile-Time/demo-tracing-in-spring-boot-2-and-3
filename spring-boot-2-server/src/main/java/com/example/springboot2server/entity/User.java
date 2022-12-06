package com.example.springboot2server.entity;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NaturalId(mutable = true)
    private String name;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
