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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleName")
    @JoinColumn(name = "role_name")
    Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionName")
    @JoinColumn(name = "permission_name")
    Permission permission;
}
