package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.NotificationReq;
import com.codewithdang.kltn_giaphaonline.dto.response.NotificationRes;
import com.codewithdang.kltn_giaphaonline.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NotificationReq notificationReq);
    
    @Mapping(target = "recipientAccountId", source = "recipient.accountId")
    @Mapping(target = "recipientName", source = "recipient.fullName")
    @Mapping(target = "senderAccountId", source = "sender.accountId")
    @Mapping(target = "senderName", source = "sender.fullName")
    NotificationRes toRes(Notification notification);
}
