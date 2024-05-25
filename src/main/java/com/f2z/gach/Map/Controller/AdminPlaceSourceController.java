package com.f2z.gach.Map.Controller;


import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.DTO.AdminPlaceRequestDTO;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Repository.BuildingFloorRepository;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/place")
@SessionAttributes
@Transactional
public class AdminPlaceSourceController {
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final PlaceSourceRepository placeSourceRepository;
    private final BuildingFloorRepository buildingFloorRepository;

    @Value("${gach.img.dir}")
    String fdir;

    @Value("${gach.path}")
    String filePath;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String placeListPage(Model model){
        model.addAttribute("placeList", placeSourceRepository.findAll());
        return "place/place-manage";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String addPlacePage(Model model){
        model.addAttribute("placeDto", new AdminPlaceRequestDTO(new PlaceSource()));
        model.addAttribute("placeCategory", PlaceCategory.values());
        return "place/place-add";
    }

    @GetMapping("/{placeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String placeDetailPage(Model model, @PathVariable Integer placeId){
        model.addAttribute("placeDto", AdminPlaceRequestDTO.toPlaceRequestDTO(placeSourceRepository.findByPlaceId(placeId)
                ,buildingFloorRepository.findAllByPlaceSource_placeId(placeId)));
        model.addAttribute("placeCategory", PlaceCategory.values());
        return "place/place-detail";
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addPlace(@ModelAttribute("placeDto") AdminPlaceRequestDTO placeDTO){
        AdminPlaceRequestDTO fileUpdatedPlaceDTO = fileSave(placeDTO);

//        if(result.hasErrors()) {
//            return "place/place-add";
//        }
        log.info(placeDTO.toString());

        PlaceSource placeSource = fileUpdatedPlaceDTO.getPlace();
        if (placeSource.getPlaceSummary() == null) {
            placeSource.setPlaceSummary(" ");
        }
        PlaceSource updatedPlaceSource = placeSourceRepository.save(placeSource);

        if(placeDTO.getBuildingFloors() != null){
            placeDTO.getBuildingFloors().forEach(floor -> {
                if(floor.getBuildingFloor() != null){
                    buildingFloorRepository.save(BuildingFloor.updateBuildingFloor(floor, updatedPlaceSource));
                }
            });
        }
        return "redirect:/admin/place/list";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updatePlace(@ModelAttribute("placeDto") AdminPlaceRequestDTO placeDto, BindingResult result){

        PlaceSource target = placeSourceRepository.findByPlaceId(placeDto.getPlace().getPlaceId());
        AdminPlaceRequestDTO fileUpdatedPlaceDTO = fileSave(placeDto);

        target.update(fileUpdatedPlaceDTO.getPlace());
        PlaceSource updatedPlaceSource = placeSourceRepository.save(target);

        buildingFloorRepository.deleteAllByPlaceSource_PlaceId(placeDto.getPlace().getPlaceId());
        if(placeDto.getBuildingFloors() != null){
            placeDto.getBuildingFloors().forEach(floor -> {
                if(floor.getBuildingFloor() != null){
                    buildingFloorRepository.save(BuildingFloor.updateBuildingFloor(floor, updatedPlaceSource));
                }
            });
        }
        return "redirect:/admin/place/list";
    }

    @GetMapping("/delete/{placeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePlace(@PathVariable Integer placeId){
        log.info("Deleting place with id {}", placeId);
        PlaceSource placeSource = placeSourceRepository.findByPlaceId(placeId);
        buildingFloorRepository.deleteAllByPlaceSource_PlaceId(placeId);
        placeSourceRepository.delete(placeSource);
        return "redirect:/admin/place/list";
    }

    public AdminPlaceRequestDTO fileSave(AdminPlaceRequestDTO placeDTO){
        try{
            if (!placeDTO.getMainFile().getOriginalFilename().equals("")) {
                log.info("여기를 거쳐요");
                log.info("placeDTO.getMainFile().getOriginalFilename(): {}!", placeDTO.getMainFile().getOriginalFilename());
                File dest = new File(fdir + "/placeImage/" + placeDTO.getMainFile().getOriginalFilename());
                placeDTO.getMainFile().transferTo(dest);
                placeDTO.getPlace().setMainImageName(dest.getName());
                placeDTO.getPlace().setMainImagePath(filePath + "/images/placeImage/" + dest.getName());
            }
            if(!placeDTO.getARFile().getOriginalFilename().equals("")) {
                File dest = new File(fdir + "/arImage/" + placeDTO.getARFile().getOriginalFilename());
                placeDTO.getARFile().transferTo(dest);
                placeDTO.getPlace().setArImageName(dest.getName());
                placeDTO.getPlace().setArImagePath(filePath + "/images/arImage/" + dest.getName());
            }
            if(!placeDTO.getThumbnailFile().getOriginalFilename().equals("")) {
                File dest = new File(fdir + "/thumbnailImage/" + placeDTO.getThumbnailFile().getOriginalFilename());
                placeDTO.getThumbnailFile().transferTo(dest);
                placeDTO.getPlace().setThumbnailImageName(dest.getName());
                placeDTO.getPlace().setThumbnailImagePath(filePath + "/images/thumbnailImage/" + dest.getName());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        log.info("placeDTO: {}", placeDTO.toString());
        return placeDTO;
    }
}
