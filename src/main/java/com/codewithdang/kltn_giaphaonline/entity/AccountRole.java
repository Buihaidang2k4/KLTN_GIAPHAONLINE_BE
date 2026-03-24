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

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "account_id",nullable = false)
    Account account;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "role_name",nullable = false)
    Role role;
}
