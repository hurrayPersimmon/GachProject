package com.f2z.gach.Inquiry.Repository;

import com.f2z.gach.Inquiry.Entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUser_userId(Long userId);

    Inquiry findByInquiryId(Integer inquiryId);

    Integer countByInquiryProgressIsFalse();

}
