package com.example.springboot2server.entity;

import com.example.commoninterface.filepermission.FilePermission;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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

    @NonNull
    @NaturalId(mutable = true)
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @Enumerated(EnumType.STRING)
    private FilePermission userPermission;

    @NonNull
    @Enumerated(EnumType.STRING)
    private FilePermission groupPermission;

    @NonNull
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
