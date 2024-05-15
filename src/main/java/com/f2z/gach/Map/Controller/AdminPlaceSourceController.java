package com.f2z.gach.Map.Controller;


import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Repository.BuildingFloorRepository;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String placeListPage(Model model){
        model.addAttribute("placeList", placeSourceRepository.findAll());
        return "place/place-manage";
    }

    @GetMapping("/add")
    public String addPlacePage(Model model){
        //FIXME : Category 작동 안함.
        model.addAttribute("placeDto", new MapDTO.PlaceSourceDTO());
        model.addAttribute("placeCategory", PlaceCategory.values());
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
        //FIXME : fileSave 메소드로 추가, 확인 후 삭제 바람.


        //placeName: test
        //placeCategory: BUILDING
        //placeSummary: test
        //placeLatitude: 37.4509531826823
        //placeLongitude: 127.12919053388762
        //placeAltitude: 55.50868988037109
        //floors[0].floorName: 1
        //floors[0].floorDesc: 123
        //thumbnail: (binary)
        //ar: (binary)
        //file: (binary)

        MapDTO.PlaceSourceDTO fileUpdatedPlaceDTO = fileSave(placeDTO);
//        if(result.hasErrors()) {
//            return "place/place-add";
//        }

        log.info(placeDTO.getBuildingFloors().toString());

        PlaceSource placeSource = MapDTO.PlaceSourceDTO.toEntity(fileUpdatedPlaceDTO);
        PlaceSource updatedPlaceSource = placeSourceRepository.save(placeSource);

        placeDTO.getBuildingFloors().stream().forEach(floor -> {
            buildingFloorRepository.save(BuildingFloor.updateBuildingFloor(floor, updatedPlaceSource));
        });
        return "redirect:/admin/place/list";
    }

    @PostMapping("/{placeId}")
    public String updatePlace(@ModelAttribute("placeDto") MapDTO.PlaceSourceDTO placeDTO,
                          @PathVariable Integer placeId){
        PlaceSource target = placeSourceRepository.findByPlaceId(placeId);
        MapDTO.PlaceSourceDTO fileUpdatedPlaceDTO = fileSave(placeDTO);

        target.update(MapDTO.PlaceSourceDTO.toEntity(fileUpdatedPlaceDTO));
        PlaceSource updatedPlaceSource = placeSourceRepository.save(target);

        buildingFloorRepository.deleteAllByPlaceSource_PlaceId(placeId);
        placeDTO.getBuildingFloors().stream().forEach(floor -> {
            buildingFloorRepository.save(BuildingFloor.updateBuildingFloor(floor, updatedPlaceSource));
        });
        return "redirect:/admin/place/list";
    }

    @GetMapping("/delete/{placeId}")
    public String deletePlace(@PathVariable Integer placeId){
        PlaceSource placeSource = placeSourceRepository.findByPlaceId(placeId);
        buildingFloorRepository.deleteAllByPlaceSource_PlaceId(placeId);
        placeSourceRepository.delete(placeSource);
        return "redirect:/admin/place/list";
    }

    public MapDTO.PlaceSourceDTO fileSave(MapDTO.PlaceSourceDTO placeDTO){
        try{
            if (placeDTO.getMainFile() != null) {
                File dest = new File(fdir + "/" + placeDTO.getMainFile().getOriginalFilename());
                placeDTO.getMainFile().transferTo(dest);
                placeDTO.setMainImageName(dest.getName());
                placeDTO.setMainImagePath(filePath + "/image/" + dest.getName());
            }else{
                placeDTO.setMainImageName(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getMainImageName());
                placeDTO.setMainImagePath(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getMainImagePath());
            }
            if(placeDTO.getARFile() != null) {
                File dest = new File(fdir + "/" + placeDTO.getARFile().getOriginalFilename());
                placeDTO.getARFile().transferTo(dest);
                placeDTO.setArImageName(dest.getName());
                placeDTO.setArImagePath(filePath + "/image/" + dest.getName());
            }else{
                placeDTO.setArImageName(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getArImageName());
                placeDTO.setArImagePath(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getArImagePath());
            }
            if(placeDTO.getThumbnailFile() != null) {
                File dest = new File(fdir + "/" + placeDTO.getThumbnailFile().getOriginalFilename());
                placeDTO.getThumbnailFile().transferTo(dest);
                placeDTO.setThumbnailImageName(dest.getName());
                placeDTO.setThumbnailImagePath(filePath + "/image/" + dest.getName());
            }else{
                placeDTO.setThumbnailImageName(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getThumbnailImageName());
                placeDTO.setThumbnailImagePath(placeSourceRepository.findByPlaceId(placeDTO.getPlaceId()).getThumbnailImagePath());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return placeDTO;
    }




}
