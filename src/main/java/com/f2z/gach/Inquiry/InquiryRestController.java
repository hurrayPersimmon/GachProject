package com.f2z.gach.Inquiry;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryRestController {
    private final InquiryService inquiryService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList() throws Exception {
        return inquiryService.getInquiryList();
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDTO.InquiryDetail> getInquiryDetailByInquiryCode(@PathVariable Integer inquiryId) {
        return inquiryService.getInquiryDetailByInquiryCode(inquiryId);
    }

}
