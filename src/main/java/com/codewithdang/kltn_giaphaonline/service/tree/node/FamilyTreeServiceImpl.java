package com.codewithdang.kltn_giaphaonline.service.tree.node;

import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRelationshipRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyTreeServiceImpl implements FamilyTreeService {

    PersonRepo personRepo;
    PersonRelationshipRepo relationshipRepo;
    FamilyCategoryRepo familyCategoryRepo;
    MinioService minioService;

    @Override
    @Transactional(readOnly = true)
    public List<FamilyTreeNodeRes> getTree(Long familyCategoryId) {
        FamilyCategory category = familyCategoryRepo.findById(familyCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));

        Long offset = category.getGenerationOffset() != null ? category.getGenerationOffset() : 1L;

        List<Person> members = personRepo.findAllByFamilyCategory_FamilyCategoryId(familyCategoryId);
        if (members.isEmpty()) return Collections.emptyList();

        List<Long> memberIds = members.stream().map(Person::getPersonId).toList();
        List<PersonRelationship> relationships = relationshipRepo.findAllByPersonIds(memberIds);

        Map<Long, List<Long>> partnerMap = new HashMap<>();
        Set<Long> partnerOutsideIds = new HashSet<>();
        Map<Long, List<Long>> childMap = new HashMap<>();

        for (PersonRelationship rel : relationships) {
            Long pid = rel.getPerson().getPersonId();
            Long partnerId = rel.getPartner().getPersonId();
            partnerMap.computeIfAbsent(pid, k -> new ArrayList<>()).add(partnerId);
            partnerMap.computeIfAbsent(partnerId, k -> new ArrayList<>()).add(pid);
            if (!memberIds.contains(partnerId)) partnerOutsideIds.add(partnerId);
            if (!memberIds.contains(pid)) partnerOutsideIds.add(pid);
        }

        // build childMap
        for (Person p : members) {
            if (p.getFather() != null) {
                childMap.computeIfAbsent(p.getFather().getPersonId(), k -> new ArrayList<>()).add(p.getPersonId());
            }
            if (p.getMother() != null) {
                childMap.computeIfAbsent(p.getMother().getPersonId(), k -> new ArrayList<>()).add(p.getPersonId());
            }
        }

        List<Person> outsidePartners = partnerOutsideIds.isEmpty()
                ? Collections.emptyList()
                : personRepo.findAllById(partnerOutsideIds);

        List<FamilyTreeNodeRes> result = new ArrayList<>();
        for (Person p : members) {
            result.add(toNode(p, partnerMap, childMap, true, offset));
        }
        for (Person p : outsidePartners) {
            result.add(toNode(p, partnerMap, childMap, false, offset));
        }

        result.sort(Comparator.comparing(FamilyTreeNodeRes::getGeneration,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(FamilyTreeNodeRes::getPersonName,
                        Comparator.nullsLast(Comparator.naturalOrder())));

        return result;
    }

    private FamilyTreeNodeRes toNode(Person p, Map<Long, List<Long>> partnerMap, Map<Long, List<Long>> childMap, boolean isInFamily, Long offset) {
        String avatarPath = p.getAvatarPath();
        String avatarUrl = avatarPath != null ? minioService.getPresignedUrl(avatarPath) : null;
        Long displayGen = p.getGeneration() != null ? p.getGeneration() + offset : null;

        return FamilyTreeNodeRes.builder()
                .id(p.getPersonId())
                .fid(p.getFather() != null ? p.getFather().getPersonId() : null)
                .mid(p.getMother() != null ? p.getMother().getPersonId() : null)
                .fidName(p.getFather() != null ? p.getFather().getFullName() : null)
                .midName(p.getMother() != null ? p.getMother().getFullName() : null)
                .pids(partnerMap.getOrDefault(p.getPersonId(), Collections.emptyList()))
                .childs(childMap.getOrDefault(p.getPersonId(), Collections.emptyList()))
                .generation(displayGen)
                .personName(p.getFullName())
                .gender(p.getGender() != null ? p.getGender().name().toLowerCase() : null)
                .avatarPath(p.getAvatarPath())
                .avatarUrl(avatarUrl)
                .biography(p.getBiography())
                .phoneNumber(p.getPhoneNumber())
                .birthDate(p.getBirthDate() != null ? p.getBirthDate() : null)
                .deathDate(p.getDeathDate() != null ? p.getDeathDate() : null)
                .lifeStatus(p.getLifeStatus() != null ? p.getLifeStatus().name() : null)
                .originPlace(p.getOriginPlace())
                .placeOfResidence(p.getPlaceOfResidence())
                .isInFamily(isInFamily)
                .build();
    }
}
