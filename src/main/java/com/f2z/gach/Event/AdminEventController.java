package com.f2z.gach.Event;

import com.f2z.gach.Event.DTOs.EventDTO;
import com.f2z.gach.Event.DTOs.EventLocationDTO;
import com.f2z.gach.Event.Repositories.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminEventController {
    private final EventRepository eventRepository;

    @Value("${gach.img.dir}")
    String fdir;

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

    @GetMapping("/event/{id}")
    public String addEventPage(@PathVariable Integer id, Model model){
        //Event객체 혹은 EventDto객체를 전송해야함.
        // 만약 Dto객체를 사용한다면 EventDto => Event로 변경사항을 반영하는 메소드도 필요
        return "event-add";
    }

    @PostMapping("/event")
    public String addEvent(@Valid @ModelAttribute("eventDto") EventDTO eventDTO, @RequestParam("file") MultipartFile file,
                           BindingResult result){
        try{
            File dest = new File(fdir+"/"+file.getOriginalFilename());
            file.transferTo(dest);
            eventDTO.setEventImageName(dest.getName());
            eventDTO.setEventImagePath("/img/"+dest.getName());
        } catch (Exception e){
            e.printStackTrace();
            log.info("이미지 저장 오류 발생");
        }
        log.info(eventDTO.toString());
        if(result.hasErrors()) {
            return "event-add";
        }

        eventRepository.save(eventDTO.toEntity());
        return "redirect:/admin/event";
    }

    // TODO : Update 메소드 필요.
    // TODO : Delete 메소드도 필요.

}
