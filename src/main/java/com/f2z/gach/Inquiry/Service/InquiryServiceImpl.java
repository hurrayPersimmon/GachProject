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

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class InquiryServiceImpl implements InquiryService{

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<InquiryResponseDTO.InquiryListStructureForClient>> getInquiryList(Long userId){
        List<Inquiry> inquiryList = inquiryRepository.findAllByUser_userId(userId);
        if(inquiryList.isEmpty()){
            if(userRepository.existsByUserId(userId)){
                return ResponseEntity.saveButNoContent(null);
            }
            else{
                return ResponseEntity.notFound(null);
            }
        }else{
            return ResponseEntity.requestSuccess(InquiryResponseDTO.toInquiryResponseListForClient(inquiryList));
        }
    }

    @Override
    public ResponseEntity<InquiryResponseDTO.saveInquirySuccess> createInquiry(InquiryRequestDTO inquiryRequestDTO) {
        if(!userRepository.existsByUserId(inquiryRequestDTO.getUserId())){
            log.info("User Not Found");
            return ResponseEntity.notFound(null);
        }
        Inquiry inquiry = inquiryRepository.save(InquiryRequestDTO.toEntity(inquiryRequestDTO, userRepository.findByUserId(inquiryRequestDTO.getUserId())));
        return ResponseEntity.saveSuccess(InquiryResponseDTO.toRespondSuccess(inquiry));
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



//    @Override
//    public ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList(Integer page, Long userId){
//        Pageable pageable = Pageable.ofSize(10).withPage(page);
//        Page<Inquiry> inquiryPage = inquiryRepository.findAllByUserId(userId, pageable);
//
//        List<InquiryResponseDTO> InquiryResponseDTOList = inquiryPage.getContent().stream()
//                .map(InquiryResponseDTO::toInquiryListResponseDTO).toList();
//
//        if(inquiryPage.isEmpty()){
//            if(userRepository.existsByUserId(userId)){
//                return ResponseEntity.saveButNoContent(null);
//            }
//            else{
//                return ResponseEntity.notFound(null);
//            }
//        }else{
//            return ResponseEntity.requestSuccess(InquiryResponseDTO.toInquiryResponseList(inquiryPage, InquiryResponseDTOList));
//        }
//    }

}
