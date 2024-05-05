package com.f2z.gach.Inquiry.Repository;

import com.f2z.gach.Inquiry.Entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAllByUserId(Long userId, Pageable pageable);

    Inquiry findByInquiryId(Integer inquiryId);

    Integer countByInquiryAnswerIsFalse();
}