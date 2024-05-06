package com.f2z.gach.Inquiry.Service;

import com.f2z.gach.Inquiry.DTO.InquiryRequestDTO;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;


public interface InquiryService {

    ResponseEntity<List<Inquiry>> getInquiryList(Long userId);

    ResponseEntity<InquiryResponseDTO.saveInquirySuccess> createInquiry(InquiryRequestDTO inquiryRequestDTO);

    ResponseEntity<InquiryResponseDTO> getInquiryDetailByInquiryId(Integer inquiryId);

}
