package com.codewithdang.kltn_giaphaonline.repo;


import com.codewithdang.kltn_giaphaonline.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepo extends JpaRepository<Family, Long> {
}