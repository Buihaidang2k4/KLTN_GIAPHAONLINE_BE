package com.codewithdang.kltn_giaphaonline.config.aspect;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogAspect {

    AuditLogService auditLogService;
    SecurityUtils securityUtils;

    @Around("@annotation(operatorAction)")
    public Object logOperatorAction(ProceedingJoinPoint joinPoint, OperatorAction operatorAction) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean isSuccess = false;
        String errorMessage = null;
        Object result = null;

        // Trích xuất thông tin HTTP request từ Context hiện tại
        HttpServletRequest request = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            request = attributes.getRequest();
        }

        String ipAddress = getClientIp(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "Unknown";
        String httpMethod = request != null ? request.getMethod() : "";
        String uri = request != null ? request.getRequestURI() : "";

        // Trích xuất thông tin Actor (An toàn tuyệt đối cho cả API public/anonymous)
        Long actorAccountId = securityUtils.getCurrentAccountOptional()
                .map(Account::getAccountId)
                .orElse(null);

        // Trích xuất metadata action & entity
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        String actionName = operatorAction.value().name() + "_" + method.getName().toUpperCase();
        String entityType = resolveEntityType(targetClass);

        // Trích xuất parameters từ request args
        Map<String, Object> paramMap = extractParams(signature.getParameterNames(), joinPoint.getArgs());
        Long familyId = extractFamilyId(paramMap);
        String entityId = extractEntityId(paramMap);

        try {
            result = joinPoint.proceed();
            isSuccess = true;
            return result;
        } catch (Throwable throwable) {
            errorMessage = throwable.getMessage();
            throw throwable;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Xây dựng log data
            Map<String, Object> logMetadata = new HashMap<>(paramMap);
            logMetadata.put("executionTimeMs", executionTime);
            logMetadata.put("httpMethod", httpMethod);
            logMetadata.put("uri", uri);
            logMetadata.put("status", isSuccess ? "SUCCESS" : "FAILED");
            if (errorMessage != null) {
                logMetadata.put("error", errorMessage);
            }

            CreateAuditLogReq auditLogReq = CreateAuditLogReq.builder()
                    .actorAccountId(actorAccountId)
                    .familyId(familyId)
                    .auditAction(actionName)
                    .entityType(entityType)
                    .entityId(entityId)
                    .newData(logMetadata)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();

            // Ghi log bất đồng bộ ngầm qua AuditLogService.log (@Async)
            try {
                auditLogService.log(auditLogReq);
            } catch (Exception e) {
                log.error("Failed to trigger audit log for action: {}", actionName, e);
            }
        }
    }

    private String resolveEntityType(Class<?> targetClass) {
        Tag tag = targetClass.getAnnotation(Tag.class);
        if (tag != null && !tag.name().isBlank()) {
            return tag.name();
        }
        String className = targetClass.getSimpleName();
        return className.replace("Controller", "").toUpperCase();
    }

    private Map<String, Object> extractParams(String[] paramNames, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        if (paramNames == null || args == null) return map;

        for (int i = 0; i < paramNames.length; i++) {
            if (args[i] != null) {
                String paramName = paramNames[i];
                // Lược bỏ HttpServletRequest / Response / MultipartFile để tránh serialize lỗi
                if (!paramName.toLowerCase().contains("request") &&
                    !paramName.toLowerCase().contains("response") &&
                    !paramName.toLowerCase().contains("file")) {
                    map.put(paramName, args[i]);
                }
            }
        }
        return map;
    }

    private Long extractFamilyId(Map<String, Object> params) {
        Object val = params.get("familyId");
        if (val instanceof Number num) {
            return num.longValue();
        }
        return null;
    }

    private String extractEntityId(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (key.endsWith("id") && !key.equals("familyid")) {
                return String.valueOf(entry.getValue());
            }
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) return "0.0.0.0";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
