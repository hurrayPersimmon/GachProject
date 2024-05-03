package com.f2z.gach.Inquiry;


import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryRestController {
    private final InquiryService inquiryService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<InquiryResponseDTO.InquiryList> getInquiryList(@RequestParam Integer page, @PathVariable Long userId) throws Exception {
        return inquiryService.getInquiryList(page, userId);
    }

    @PostMapping()
    public ResponseEntity<InquiryResponseDTO> createInquiry(@RequestBody InquiryRequestDTO inquiryRequestDTO) {
        return inquiryService.createInquiry(inquiryRequestDTO);
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDTO> getInquiryDetailByInquiryId(@PathVariable Integer inquiryId) {
        return inquiryService.getInquiryDetailByInquiryId(inquiryId);
    }

}
