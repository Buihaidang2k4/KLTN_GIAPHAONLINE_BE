package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "account_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRole {
    @EmbeddedId
    AccountRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleName")
    @JoinColumn(name = "role_name")
    Role role;
}
