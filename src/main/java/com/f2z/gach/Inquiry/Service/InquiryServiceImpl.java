package com.f2z.gach.Inquiry.Service;

import com.f2z.gach.Inquiry.DTO.InquiryRequestDTO;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class InquiryServiceImpl implements InquiryService{

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList(Integer page, Long userId){
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<InquiryResponseDTO> inquiryPage = inquiryRepository.findAllByUserId(userId, pageable).map(InquiryResponseDTO::toInquiryListResponseDTO);
        if(inquiryPage.isEmpty()){
            if(userRepository.existsByUserId(userId)){
                return ResponseEntity.saveButNoContent(null);
            }
            else{
                return ResponseEntity.notFound(null);
            }
        }
        return ResponseEntity.requestSuccess((InquiryResponseDTO.InquiryList) inquiryPage.getContent());
    }

    @Override
    public ResponseEntity<InquiryResponseDTO> getInquiryDetailByInquiryId(Integer inquiryId) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId);
        if(inquiry != null){
            return ResponseEntity.requestSuccess(InquiryResponseDTO.toInquiryResponseDTO(inquiry));
        }
        else{
            return ResponseEntity.notFound(null);
        }
    }

    @Override
    public ResponseEntity<InquiryResponseDTO> createInquiry(InquiryRequestDTO inquiryRequestDTO) {
        Inquiry inquiry = inquiryRepository.save(InquiryRequestDTO.toEntity(inquiryRequestDTO));
        return ResponseEntity.saveSuccess(InquiryResponseDTO.toRespondSuccess(inquiry));
    }
}
