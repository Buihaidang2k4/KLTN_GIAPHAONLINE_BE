package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionId implements Serializable {
    static final long serialVersionUID = 1L;

    @Column(name = "role_name")
    String roleName;

    @Column(name = "permission_name")
    String permissionName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermissionId that = (RolePermissionId) o;
        return Objects.equals(roleName, that.roleName) && Objects.equals(permissionName, that.permissionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, permissionName);
    }
}
