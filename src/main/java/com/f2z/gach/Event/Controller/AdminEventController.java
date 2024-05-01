package com.f2z.gach.Event.Controller;

import com.f2z.gach.Event.DTO.EventDTO;
import com.f2z.gach.Event.Repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminEventController {
    private final EventRepository eventRepository;

    @GetMapping("/event")
    public String eventListPage(Model model){
        model.addAttribute("eventList", eventRepository.findAll());
        return "event-manage";
    }

    @GetMapping("/event/add")
    public String addEventPage(Model model){
        model.addAttribute("eventDto", new EventDTO());
        return "event-add";
    }

    @PostMapping("/event")
    public String addEvent(@Valid @ModelAttribute("eventDto") EventDTO eventDTO,
                           BindingResult result,
                           Model model){
        if(result.hasErrors()){
            return "event-add";
        }else if(eventRepository.existsByEventName(eventDTO.getEventName())){
            model.addAttribute("errorMessage", "이미 존재하는 이벤트입니다.");
            return "event-add";
        }

        eventRepository.save(eventDTO.toEntity());
        model.addAttribute("message", "이벤트가 추가되었습니다.");
        model.addAttribute("eventList", eventRepository.findAll());
        return "event-manage";
    }

}
