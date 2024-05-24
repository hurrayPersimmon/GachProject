package com.f2z.gach.Inquiry.Repository;

import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.Inquiry.DTO.chartDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUser_userId(Long userId);

    Inquiry findByInquiryId(Integer inquiryId);

    Integer countByInquiryProgressIsFalse();

    List<Inquiry> findAllByCreateDtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Inquiry> findByInquiryProgressFalse(Pageable pageable);

    List<Inquiry> findAllByInquiryCategory(InquiryCategory category);

    @Query("SELECT new com.f2z.gach.Inquiry.DTO.chartDTO(DATE(i.createDt), COUNT(i)) " +
            "FROM Inquiry i " +
            "WHERE i.createDt >= :startDate AND i.createDt < :endDate " +
            "GROUP BY DATE(i.createDt) " +
            "ORDER BY DATE(i.createDt)")
    List<chartDTO> findDailyInquiryCounts(LocalDateTime startDate, LocalDateTime endDate);

    List<Inquiry> findAllByCreateDtBetweenAndInquiryCategory(LocalDateTime startDate, LocalDateTime endDate, InquiryCategory category);

    @Query("SELECT p FROM Inquiry p WHERE p.inquiryTitle LIKE %?1%")
    Page<Inquiry> findAllByInquiryTitleContaining(String sort, Pageable pageable);
}
