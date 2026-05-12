package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PersonMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonServiceImpl implements PersonService {

    PersonRepo personRepo;
    FamilyCategoryRepo familyCategoryRepo;
    AccountRepo accountRepo;
    PersonMapper personMapper;

    @Override
    @Transactional
    public PersonRes createPerson(Long categoryId, PersonReq req) {
        FamilyCategory category = familyCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));

        Account account = getCurrentAccount();

        Person person = personMapper.toEntity(req);
        person.setFamilyCategory(category);
        person.setCreatedByAccount(account);

        return personMapper.toRes(personRepo.save(person));
    }

    @Override
    @Transactional
    public PersonRes updatePerson(Long personId, PersonReq req) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
        personMapper.updateEntityFromRequest(req, person);

        return personMapper.toRes(personRepo.save(person));
    }

    @Override
    @Transactional(readOnly = true)
    public PersonRes getPersonById(Long personId) {
        return personRepo.findById(personId)
                .map(personMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public PersonRes getPersonByFamilyCategoryId(Long familyCategoryId) {
        // trả về root person (không có cha/mẹ) của category
        return personRepo.findAllByFamilyCategory_FamilyCategoryId(familyCategoryId)
                .stream()
                .filter(p -> p.getFather() == null && p.getMother() == null)
                .findFirst()
                .map(personMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    }

    private Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return accountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
