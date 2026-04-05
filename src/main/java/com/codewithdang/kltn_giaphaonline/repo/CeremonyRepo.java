package com.codewithdang.kltn_giaphaonline.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Ceremony extends JpaRepository<Ceremony, Long> {
}
