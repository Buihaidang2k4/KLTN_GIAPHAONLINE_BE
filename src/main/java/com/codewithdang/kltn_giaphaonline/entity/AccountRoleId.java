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
public class AccountRoleId implements Serializable {

    static final long serialVersionUID = 1L;

    @Column(name = "account_id")
    Long accountId;

    @Column(name = "role_name")
    String roleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRoleId that = (AccountRoleId) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, roleName);
    }
}
