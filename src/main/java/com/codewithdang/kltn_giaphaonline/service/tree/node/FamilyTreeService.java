package com.codewithdang.kltn_giaphaonline.service.tree.node;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;

import java.util.List;

public interface FamilyTreeService {
    List<FamilyTreeNodeRes> getTree(Long familyCategoryId);
}
