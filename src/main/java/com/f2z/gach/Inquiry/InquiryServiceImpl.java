package com.f2z.gach.Inquiry;

import com.f2z.gach.Response.ResponseEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class InquiryServiceImpl implements InquiryService{

    @Override
    public ResponseEntity<InquiryResponseDTO.InquiryList> getEventList() {
        return null;
    }

    @Override
    public ResponseEntity<List<Inquiry>> getEventLocationByEventCode(Integer eventCode) {
        return null;
    }
}
