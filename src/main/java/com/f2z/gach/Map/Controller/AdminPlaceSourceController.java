package com.f2z.gach.Map.Controller;


import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/place")
@SessionAttributes
public class AdminPlaceSourceController {
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final PlaceSourceRepository placeSourceRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/list")
    public String placeListPage(Model model){
        model.addAttribute("placeList", placeSourceRepository.findAll());
        return "place/place-manage";
    }

    @GetMapping("/add")
    public String addPlacePage(Model model){
        model.addAttribute("placeDto", new MapDTO.PlaceSourceDTO());
        //Category Enum 클래스를 따로 model로 전달해야함. 영어로 되어 있던데, 문의 Enum처럼 하면 좋을 것 같음
        return "place/place-add";
    }

    @GetMapping("/{placeId}")
    public String placeDetailPage(Model model, @PathVariable Integer placeId){
        model.addAttribute("placeDto", placeSourceRepository.findByPlaceId(placeId));
        return "place/place-detail";
    }

    @PostMapping()
    public String addPlace(@ModelAttribute("placeDto") MapDTO.PlaceSourceDTO placeDTO){
        PlaceSource placeSource = MapDTO.PlaceSourceDTO.toEntity(placeDTO);
        placeSourceRepository.save(placeSource);
        return "redirect:/admin/place/list";
    }

    @PostMapping("/{placeId}")
    public String updatePlace(@ModelAttribute("placeDto") MapDTO.PlaceSourceDTO placeDTO,
                          @PathVariable Integer placeId){
        log.info(placeDTO.toString());
        PlaceSource target = placeSourceRepository.findByPlaceId(placeId);
        target.update(MapDTO.PlaceSourceDTO.toEntity(placeDTO));
        placeSourceRepository.save(target);
        return "redirect:/admin/place/list";
    }

    @GetMapping("/delete/{placeId}")
    public String deletePlace(@PathVariable Integer placeId){
        PlaceSource placeSource = placeSourceRepository.findByPlaceId(placeId);
        placeSourceRepository.delete(placeSource);
        return "redirect:/admin/place/list";
    }




}
