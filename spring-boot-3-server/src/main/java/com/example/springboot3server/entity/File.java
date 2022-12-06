package com.example.springboot3server.entity;

import com.example.commoninterface.filepermission.FilePermission;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Entity
@Table(name = "files")

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NaturalId(mutable = true)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FilePermission userPermission;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FilePermission groupPermission;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FilePermission otherPermission;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final File file = (File) o;
        return name.equals(file.name) && user.equals(file.user) && group.equals(file.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user, group);
    }
}
