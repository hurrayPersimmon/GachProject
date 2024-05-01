package com.f2z.gach.Inquiry;

import com.f2z.gach.Event.Service.EventServiceImpl;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;


public interface InquiryService {
    ResponseEntity<InquiryResponseDTO.InquiryList> getEventList();

    ResponseEntity<List<Inquiry>> getEventLocationByEventCode(Integer eventCode);
}
