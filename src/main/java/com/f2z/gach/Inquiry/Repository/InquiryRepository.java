package com.f2z.gach.Inquiry.Repository;

import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUser_userId(Long userId);

    Inquiry findByInquiryId(Integer inquiryId);

    Integer countByInquiryProgressIsFalse();

    Page<Inquiry> findByInquiryProgressFalse(Pageable pageable);

    List<Inquiry> findAllByInquiryCategory(InquiryCategory category);

    @Query("SELECT p FROM Inquiry p WHERE p.inquiryTitle LIKE %?1%")
    Page<Inquiry> findAllByInquiryTitleContaining(String sort, Pageable pageable);
}
