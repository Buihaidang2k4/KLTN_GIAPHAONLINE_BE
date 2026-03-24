package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission {

    @EmbeddedId
    RolePermissionId id;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "role_name",nullable = false)
    Role role;

    @MapsId("permissionName")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "permission_name",nullable = false)
    Permission permission;
}
