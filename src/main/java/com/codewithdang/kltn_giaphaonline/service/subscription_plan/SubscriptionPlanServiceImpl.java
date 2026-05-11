package com.codewithdang.kltn_giaphaonline.service.subscription_plan;

import com.codewithdang.kltn_giaphaonline.dto.request.SubscriptionPlanReq;
import com.codewithdang.kltn_giaphaonline.dto.response.SubscriptionPlanRes;
import com.codewithdang.kltn_giaphaonline.entity.SubscriptionPlan;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.SubscriptionPlanMapper;
import com.codewithdang.kltn_giaphaonline.repo.SubscriptionPlanRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    SubscriptionPlanRepo planRepo;
    SubscriptionPlanMapper planMapper;

    @Override
    @Transactional
    public SubscriptionPlanRes createPlan(SubscriptionPlanReq request) {
        if (planRepo.existsByCode(request.getCode()))
            throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_CODE_IS_EXISTED);

        SubscriptionPlan plan = planMapper.toEntity(request);
        plan.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        planRepo.save(plan);
        return planMapper.toRes(plan);
    }

    @Override
    @Transactional
    public SubscriptionPlanRes createPlanDefault() {
        // Check if FREE plan already exists
        if (planRepo.existsByCode("FREE")) {
            log.info("FREE plan already exists");
            return planRepo.findByCode("FREE")
                    .map(planMapper::toRes)
                    .orElse(null);
        }

        // Create default FREE plan
        SubscriptionPlan defaultPlan = SubscriptionPlan.builder()
                .namePlan("Gói Khởi Đầu")
                .code("FREE")
                .price(BigDecimal.ZERO)
                .currency("VND")
                .maxAdmin(1)
                .maxPerson(200)
                .maxStorageMb(1024)
                .durationMonth(12)
                .isActive(true)
                .build();

        planRepo.save(defaultPlan);
        log.info("Default FREE plan created successfully");
        return planMapper.toRes(defaultPlan);
    }

    @Transactional(readOnly = true)
    public SubscriptionPlan getOrCreateDefaultPlan() {
        return planRepo.findByCode("FREE")
                .orElseGet(() -> {
                    log.warn("FREE plan not found! Creating default plan...");
                    SubscriptionPlan defaultPlan = SubscriptionPlan.builder()
                            .namePlan("Gói Khởi Đầu")
                            .code("FREE")
                            .price(BigDecimal.ZERO)
                            .currency("VND")
                            .maxAdmin(1)
                            .maxPerson(200)
                            .maxStorageMb(1024)
                            .durationMonth(12)
                            .isActive(true)
                            .build();
                    return planRepo.save(defaultPlan);
                });
    }

    @Override
    @Transactional
    public SubscriptionPlanRes updatePlan(Long planId, SubscriptionPlanReq request) {
        SubscriptionPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));

        if (!plan.getCode().equals(request.getCode()) && planRepo.existsByCode(request.getCode()))
            throw new AppException(ErrorCode.SUBSCRIPTION_PLAN_CODE_IS_EXISTED);

        planMapper.updatePlanFromRequest(request, plan);
        plan.setIsActive(request.getIsActive() != null ? request.getIsActive() : plan.getIsActive());
        planRepo.save(plan);
        return planMapper.toRes(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanRes getPlanById(Long planId) {
        return planRepo.findById(planId).map(planMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanRes> getAllPlans() {
        return planRepo.findAll()
                .stream()
                .map(planMapper::toRes)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanRes> getActivePlans() {
        return planRepo.findByIsActiveTrue()
                .stream()
                .map(planMapper::toRes)
                .toList();
    }

    @Override
    @Transactional
    public void deletePlan(Long planId) {
        SubscriptionPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
        planRepo.delete(plan);
    }
}
