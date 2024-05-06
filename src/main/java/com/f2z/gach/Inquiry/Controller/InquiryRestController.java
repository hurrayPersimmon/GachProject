package com.f2z.gach.Inquiry.Controller;


import com.f2z.gach.Inquiry.DTO.InquiryRequestDTO;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Service.InquiryService;
import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryRestController {
    private final InquiryService inquiryService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<Inquiry>> getInquiryList(@PathVariable Long userId) throws Exception {
        return inquiryService.getInquiryList(userId);
    }

    @PostMapping()
    public ResponseEntity<InquiryResponseDTO> createInquiry(@RequestBody InquiryRequestDTO inquiryRequestDTO) {
        return inquiryService.createInquiry(inquiryRequestDTO);
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDTO> getInquiryDetailByInquiryId(@PathVariable Integer inquiryId) {
        return inquiryService.getInquiryDetailByInquiryId(inquiryId);
    }

//    @GetMapping("/list/{userId}")
//    public ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList(@RequestParam Integer page, @PathVariable Long userId) throws Exception {
//        return inquiryService.getInquiryList(page, userId);
//    }
//

}
