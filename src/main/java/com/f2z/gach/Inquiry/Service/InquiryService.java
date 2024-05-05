package com.f2z.gach.Inquiry.Service;

import com.f2z.gach.Inquiry.DTO.InquiryRequestDTO;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Response.ResponseEntity;


public interface InquiryService {

    ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList(Integer page, Long userId);

    ResponseEntity<InquiryResponseDTO> getInquiryDetailByInquiryId(Integer inquiryId);

    ResponseEntity<InquiryResponseDTO> createInquiry(InquiryRequestDTO inquiryRequestDTO);
}
