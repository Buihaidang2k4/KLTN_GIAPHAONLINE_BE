package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @Column(name = "name", length = 100)
    String name;

    String description;

    @Builder.Default
    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions = new HashSet<>();
}
