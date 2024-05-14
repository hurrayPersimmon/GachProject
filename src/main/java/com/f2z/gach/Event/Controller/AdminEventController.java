package com.f2z.gach.Event.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Event.DTO.*;
import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Event.Repository.EventLocationRepository;
import com.f2z.gach.Event.Repository.EventRepository;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/event")
@SessionAttributes
public class AdminEventController {
    private final EventRepository eventRepository;
    private final AdminRepository adminRepository;
    private final EventLocationRepository eventLocationRepository;
    private final InquiryRepository inquiryRepository;

    @Value("${gach.img.dir}")
    String fdir;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());

    }

    @GetMapping("/list/{page}")
    public String eventListPage(Model model, @PathVariable Integer page){
        Pageable pageable = Pageable.ofSize(10).withPage(page);
        Page<Event> eventPage = eventRepository.findAllBy(pageable);
        List<EventResponseDTO.AdminEventListStructure> eventResponseDTOList = eventPage.getContent().stream()
                .map(EventResponseDTO.AdminEventListStructure::toAdminEventListStructure).toList();
        model.addAttribute("eventList", EventResponseDTO.toAdminEventList(eventPage, eventResponseDTOList));

        return "event/event-manage";
    }

    @GetMapping("/add")
    public String addEventPage(Model model){
        model.addAttribute("eventDto", new AdminEventRequestDTO());
        return "event/event-add";
    }

    @GetMapping("/{eventId}")
    public String updateEventPage(@PathVariable Integer eventId, Model model){

        model.addAttribute("eventDto", AdminEventRequestDTO
                .toEventRequestDTO(eventRepository.findByEventId(eventId),
                eventLocationRepository.findAllByEvent_EventId(eventId)));
        return "event/event-detail";
    }

    @PostMapping()
    public String addEvent(@Valid @ModelAttribute AdminEventRequestDTO requestDTO,
                           BindingResult result){
        try{
            File dest = new File(fdir+"/"+requestDTO.getFile().getOriginalFilename());
            requestDTO.getFile().transferTo(dest);
            requestDTO.getEvent().setEventImageName(dest.getName());
            requestDTO.getEvent().setEventImagePath("/image/"+dest.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        if(result.hasErrors()) {
            return "event/event-add";
        }

        Event event = requestDTO.getEvent();

        eventRepository.save(event);
        requestDTO.getLocations().stream().forEach(i -> {
            if(i.getEventLatitude() != null){
                eventLocationRepository.save(EventLocation.updateEventLocation(i, event));
            }
            log.info(i.toString());
        });
        return "redirect:/admin/event/list/0";
    }

    @PostMapping("/update")
    public String updateEvent(@Valid @ModelAttribute("eventDto") AdminEventRequestDTO requestDTO,
                              BindingResult result){
        if(result.hasErrors()){
            return "event/event-detail";
        }
        log.info(requestDTO.toString());
        Event event = eventRepository.findByEventId(requestDTO.getEvent().getEventId());
        event.update(requestDTO.getEvent());
        Event updatedEvent = eventRepository.save(event);
        requestDTO.getLocations().stream().forEach(i -> {
            if(i.getEventLatitude() != null){
                if(i.getEventLocationId() == null){
                    eventLocationRepository.save(EventLocation.updateEventLocation(i, updatedEvent));
                }else{
                    EventLocation eventLocation = eventLocationRepository.findByEventLocationId(i.getEventLocationId());
                    eventLocation.update(i.updateEventLocation(i, updatedEvent));
                    eventLocationRepository.save(eventLocation);
                }
            }
            log.info(i.toString());
        });
        return "redirect:/admin/event/list/0";
    }

    @GetMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable Integer eventId){
        Event event = eventRepository.findByEventId(eventId);
        eventRepository.delete(event);
        return "redirect:/admin/event/list/0";
    }



    // TODO : Update 메소드 필요.
    // TODO : Delete 메소드도 필요.
}
