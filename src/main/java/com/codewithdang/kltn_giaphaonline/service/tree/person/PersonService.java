package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;

public interface PersonService {
    PersonRes createPerson(Long categoryId, PersonReq req);

    PersonRes updatePerson(Long personId, PersonReq req);

    PersonRes getPersonById(Long personId);

    PersonRes getPersonByFamilyCategoryId(Long familyCategoryId);

    // Tạo thủy tổ (cha) cho node đang là gốc, chỉ node nam không có cha mới được
    FamilyTreeNodeRes addRoot(Long personId, PersonReq req);

    // Thêm vợ/chồng cho personId
    FamilyTreeNodeRes addPartner(Long personId, PersonReq req);

    // Thêm con cho personId, tự xác định fid/mid theo gender
    FamilyTreeNodeRes addChild(Long personId, PersonReq req);

    // Xóa person, chỉ được xóa nếu không có con
    void deletePerson(Long personId);
}
