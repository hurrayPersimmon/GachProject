package com.f2z.gach.Map.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Log.Repository.LogRepository;
import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
@Transactional
public class AdminLineController {
    private final MapLineRepository mapLineRepository;
    private final MapNodeRepository mapNodeRepository;
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final LogRepository logRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
        model.addAttribute("lineSize", mapLineRepository.count());
    }

    @GetMapping("/line/list/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String lineListPage(Model model, @PathVariable Integer page){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC,"lineId").withPage(page);
        Page<MapLine> linePage = mapLineRepository.findAll(pageable);
        List<MapDTO.MapLineListStructure> lineList = linePage.getContent().stream()
                .map(MapDTO.MapLineListStructure::toMapLineListStructure).toList();
        model.addAttribute("lineList", MapDTO.toMapLineList(linePage, lineList));
        model.addAttribute("lineChartData", mapLineRepository.findAll());
        model.addAttribute("inquiryList", getInquiry(
                getDateRange(), inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusDays(6), LocalDateTime.now())));
        model.addAttribute("lineInquiryList", getInquiry(
                getDateRange(), inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusDays(6), LocalDateTime.now())));
        model.addAttribute("lineSatisList", getSatisfactionMap());
        model.addAttribute("lineCnt", logRepository.countLogsByDateAndUrl(
                LocalDateTime.now().minusDays(6).with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX), "GET", "/map/find?%"));
        return "line/line-manage";
    }

    @GetMapping("/line/sortedlist/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String lineListSortedPage(Model model, @PathVariable Integer page, @RequestParam String sort){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC, "lineId").withPage(page);
        Page<MapLine> linePage = mapLineRepository.findAllByNodeNameContaining(sort, pageable);
        List<MapDTO.MapLineListStructure> lineList = linePage.getContent().stream()
                .map(MapDTO.MapLineListStructure::toMapLineListStructure).toList();
        model.addAttribute("lineList", MapDTO.toMapLineList(linePage, lineList));
        model.addAttribute("lineChartData", mapLineRepository.findAll());
        model.addAttribute("inquiryList", getInquiry(
                getDateRange(), inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusDays(6), LocalDateTime.now())));
        model.addAttribute("lineInquiryList", getInquiry(
                getDateRange(), inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusDays(6), LocalDateTime.now())));
        model.addAttribute("lineSatisList", getSatisfactionMap());
        model.addAttribute("lineCnt", logRepository.countLogsByDateAndUrl(
                LocalDateTime.now().minusDays(6).with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX), "GET", "/map/find?%"));
        return "line/line-manage";
    }

    @GetMapping("/line/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String addLinePage(Model model){
        model.addAttribute("lineDto", new MapDTO.MapLineDTO());
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        model.addAttribute("lineNode", getMapNodePairs());
        return "line/line-add";
    }

    @PostMapping("/line")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addLine(@Valid @ModelAttribute("lineDto") MapDTO.MapLineDTO mapLineDTO,
                          BindingResult result){
        if(result.hasErrors()){
            return "line/line-add";
        }
        mapLineDTO.setNodeFirst(mapNodeRepository.findByNodeId(mapLineDTO.getNodeFirstId()));
        mapLineDTO.setNodeSecond(mapNodeRepository.findByNodeId(mapLineDTO.getNodeSecondId()));
        mapLineRepository.save(mapLineDTO.toSaveEntity("A",
                mapLineDTO.getNodeFirst(), mapLineDTO.getNodeSecond()));
        mapLineRepository.save(mapLineDTO.toSaveEntity("B",
                mapLineDTO.getNodeSecond(), mapLineDTO.getNodeFirst()));
        return "redirect:/admin/line/list/0";
    }

    @GetMapping("/line/{lineId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteLine(@PathVariable Integer lineId,
                             Model model)throws Exception{
        if(mapLineRepository.existsByLineId(lineId)){
            mapLineRepository.deleteByLineId(lineId);
            if(lineId%2==0) lineId-=1;
            else lineId+=1;
            mapLineRepository.deleteByLineId(lineId);
            model.addAttribute("message", "간선이 삭제되었습니다.");
            model.addAttribute("lineList", mapLineRepository.findAll());
            return "redirect:/admin/line/list/0";
        }
        throw new Exception();
    }

    Map<String, Long> getInquiry(List<LocalDate> dateRange, List<Inquiry> inquiryList){
        Map<String, Long> result = new LinkedHashMap<>();
        Map<String, Long> inquiryCountByDate = inquiryList.stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getCreateDt().toLocalDate().toString(),
                        Collectors.counting()
                ));
        for (LocalDate date : dateRange) {
            String dateString = date.toString();
            Long inquiryCount = inquiryCountByDate.getOrDefault(dateString, 0L);
            result.put(dateString, inquiryCount);
        }
        return result;
    }

    Map<String, Integer> getTopModes(){
        Map<String, Integer> map = new LinkedHashMap<>();
        for(Object[] objects : userHistoryRepository.findTopMapNodes(5)){
            int nodeId = Integer.parseInt(objects[0].toString());
            int value = Integer.parseInt(objects[1].toString());
            map.put(mapNodeRepository.findByNodeId(nodeId).getNodeName(), value);
        }
        return map;
    }

    List<LocalDate> getDateRange(){
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusDays(6).plusDays(i).toLocalDate();
            dateRange.add(date);
        }

        return dateRange;
    }

    Map<LocalDate, Double> getSatisfactionMap(){
        Map<LocalDate, Double> map = new LinkedHashMap<>();
        for(Object[] objects : userHistoryRepository.findAverageSatisfactionRouteByDateRange(LocalDateTime.now().minusDays(5), LocalDateTime.now())){
            Date sqlDate = (Date) objects[0];
            LocalDate date = Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            Double averSatisfaction = (Double) objects[1];
            map.put(date, averSatisfaction);
        }
        return map;
    }

    List<MapNode[]> getMapNodePairs(){
        List<MapNode[]> mapNodePairs = new ArrayList<>();
        for (MapLine mapLine : mapLineRepository.findAll()) {
            MapNode[] nodePair = {mapLine.getNodeFirst(), mapLine.getNodeSecond()};
            mapNodePairs.add(nodePair);
        }
        return mapNodePairs;
    }
}
