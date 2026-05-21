package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AuditLogRes;
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
    @Mapping(target = "action", source = "auditAction")
    @Mapping(target = "entityType", source = "entityType")
    @Mapping(target = "entityId", source = "entityId")
    @Mapping(target = "familyId", source = "familyId")
    @Mapping(target = "oldData", source = "oldData")
    @Mapping(target = "newData", source = "newData")
    @Mapping(target = "ipAddress", source = "ipAddress")
    @Mapping(target = "userAgent", source = "userAgent")
    AuditLog toEntity(CreateAuditLogReq req);

    @Mapping(target = "actorEmail", source = "actor.email")
    AuditLogRes toRes(AuditLog auditLog);

    @Named("mapActor")
    default Account mapActor(Long accountId) {
        if (accountId == null) return null;

        Account account = new Account();
        account.setAccountId(accountId);
        return account;
    }
}
