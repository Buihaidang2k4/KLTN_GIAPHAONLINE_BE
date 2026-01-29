package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @Column(name = "name", length = 50)
    String name;

    String description;

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions = new HashSet<>();
}
