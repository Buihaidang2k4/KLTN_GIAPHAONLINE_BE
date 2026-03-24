package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.RoleScopeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.LinkedHashSet;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false, length = 20)
    RoleScopeType scopeType;

    String description;

    @Builder.Default
    @OneToMany(mappedBy = "role")
    Set<AccountRole> accountRoles = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "role")
    Set<RolePermission> rolePermissions = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "role")
    Set<FamilyMember> familyMembers = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "role")
    Set<FamilyInvitation> familyInvitations = new LinkedHashSet<>();
}
