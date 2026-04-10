package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "actor", source = "actorAccountId", qualifiedByName = "mapActor")
    @Mapping(target = "action", expression = "java(req.action() != null ? req.action().name() : null)")
    AuditLog toEntity(CreateAuditLogReq req);

    @Named("mapActor")
    default Account mapActor(Long accountId) {
        if (accountId == null) return null;

        Account account = new Account();
        account.setAccountId(accountId);
        return account;
    }
}
