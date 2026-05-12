package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonReq req);

    @Mapping(target = "familyCategoryId", source = "familyCategory.familyCategoryId")
    @Mapping(target = "rootPersonId", source = "rootPerson.personId")
    @Mapping(target = "fatherId", source = "father.personId")
    @Mapping(target = "motherId", source = "mother.personId")
    @Mapping(target = "createdByAccountId", source = "createdByAccount.accountId")
    PersonRes toRes(Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(PersonReq req, @MappingTarget Person person);
}
