package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;

public interface PersonService {
    PersonRes createPerson(Long categoryId, PersonReq req);

    PersonRes updatePerson(Long personId, PersonReq req);

    PersonRes getPersonById(Long personId);

    PersonRes getPersonByFamilyCategoryId(Long familyCategoryId);
}
