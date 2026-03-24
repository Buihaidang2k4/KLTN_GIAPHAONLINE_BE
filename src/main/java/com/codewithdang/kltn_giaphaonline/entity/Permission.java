package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    @Id
    @Column(name = "name", length = 100)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false)
    RoleScopeType scopeType;

    String description;

    @Builder.Default
    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions = new HashSet<>();
}
