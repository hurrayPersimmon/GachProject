package com.f2z.gach.Map.Controller;

import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.EnumType.InquiryCategory;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Inquiry.Entity.Inquiry;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.DTO.MapDTO;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
@Transactional
public class AdminNodeController {
    private final MapNodeRepository mapNodeRepository;
    private final MapLineRepository mapLineRepository;
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final UserHistoryRepository userHistoryRepository;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
    }

    @GetMapping("/node/list/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String nodeListPage(Model model, @PathVariable Integer page){

        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC,"nodeId").withPage(page);
        Page<MapNode> nodePage = mapNodeRepository.findAll(pageable);
        List<MapDTO.MapNodeListStructure> nodeList = nodePage.getContent().stream()
                .map(MapDTO.MapNodeListStructure::toMapNodeListStructure)
                .collect(Collectors.toList());
        List<Inquiry> allInquiryList = inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusDays(6), LocalDateTime.now());
        List<Inquiry> nodeInquiryList = inquiryRepository.findAllByCreateDtBetweenAndInquiryCategory(LocalDateTime.now().minusDays(6), LocalDateTime.now(), InquiryCategory.Node);
        model.addAttribute("nodeList", MapDTO.toMapNodeList(nodePage, nodeList));
        model.addAttribute("nodeChartData", mapNodeRepository.findAll());

        List<LocalDate> dateRange = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusDays(6).plusDays(i).toLocalDate();
            dateRange.add(date);
        }

        Map<String, Long> inquiryCountByDate = allInquiryList.stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getCreateDt().toLocalDate().toString(),
                        Collectors.counting()
                ));

        Map<String, Long> result = new LinkedHashMap<>();
        for (LocalDate date : dateRange) {
            String dateString = date.toString();
            Long inquiryCount = inquiryCountByDate.getOrDefault(dateString, 0L);
            result.put(dateString, inquiryCount);
        }

        model.addAttribute("inquiryList", result);

        Map<String, Long> inquiryCountByDate2 = nodeInquiryList.stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getCreateDt().toLocalDate().toString(),
                        Collectors.counting()
                ));

        Map<String, Long> result2 = new LinkedHashMap<>();
        for (LocalDate date : dateRange) {
            String dateString = date.toString();
            Long inquiryCount = inquiryCountByDate2.getOrDefault(dateString, 0L);
            result2.put(dateString, inquiryCount);
        }

        model.addAttribute("nodeInquiryList", result2);
        Map<String, Integer> map = new LinkedHashMap<>();
        for(Object[] objects : userHistoryRepository.findTopMapNodes(5)){
            int nodeId = Integer.parseInt(objects[0].toString());
            int value = Integer.parseInt(objects[1].toString());
            map.put(mapNodeRepository.findByNodeId(nodeId).getNodeName(), value);
        }

        model.addAttribute("map", map);
        model.addAttribute("countNodes", mapNodeRepository.countNodesNotInLines());
        return "node/node-manage";
    }

    @GetMapping("/node/sortedlist/{page}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String nodeListSortedPage(Model model, @PathVariable Integer page, @RequestParam String sort){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.ASC, "nodeId").withPage(page);
        Page<MapNode> nodePage = mapNodeRepository.findAllByNodeNameContaining(sort, pageable);
        List<MapDTO.MapNodeListStructure> nodeList = nodePage.getContent().stream()
                .map(MapDTO.MapNodeListStructure::toMapNodeListStructure)
                .collect(Collectors.toList());
        model.addAttribute("nodeList", MapDTO.toMapNodeList(nodePage, nodeList));
        model.addAttribute("nodeChartData", mapNodeRepository.findAll());
        model.addAttribute("countNodes", mapNodeRepository.countNodesNotInLines());
        List<Inquiry> allInquiryList = inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
        List<Inquiry> nodeInquiryList = inquiryRepository.findAllByCreateDtBetweenAndInquiryCategory(LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), InquiryCategory.Node);
        model.addAttribute("nodeList", MapDTO.toMapNodeList(nodePage, nodeList));
        model.addAttribute("nodeChartData", mapNodeRepository.findAll());
        model.addAttribute("inquiryList", allInquiryList.stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getCreateDt().toLocalDate().toString(),
                        Collectors.counting()
                ))
        );
        model.addAttribute("nodeInquiryList", nodeInquiryList.stream()
                .collect(Collectors.groupingBy(
                        inquiry -> inquiry.getCreateDt().toLocalDate().toString(),
                        Collectors.counting()
                ))
        );
        model.addAttribute("unSatisfaction", userHistoryRepository.findBottomMapNodes(5));
        return "node/node-manage";
    }

    @GetMapping("/node/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String addNodePage(Model model){
        model.addAttribute("nodeDto", new MapDTO.MapNodeDTO());
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node/node-add";
    }

    @PostMapping("/node")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapNodeDTO,
                          BindingResult result){
        if(result.hasErrors()){
            return "node/node-add";
        }
        mapNodeRepository.save(mapNodeDTO.toEntity());
        return "redirect:/admin/node/list/0";
    }

    @GetMapping("/node/{nodeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GUEST')")
    public String updateNodePage(@PathVariable Integer nodeId, Model model){
        MapNode mapNode = mapNodeRepository.findByNodeId(nodeId);
        model.addAttribute("nodeDto", mapNode);
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node/node-detail";
    }

    @PostMapping("/node/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapDTO,
                             BindingResult result){
        if(result.hasErrors()){
            log.info("오류");
            return "node/node-detail";
        }
        MapNode mapNode = mapNodeRepository.findByNodeId(mapDTO.toEntity().getNodeId());
        mapNode.update(mapDTO.toEntity());
        mapNodeRepository.save(mapNode);
        return "redirect:/admin/node/list/0";
    }

    @GetMapping("/node/delete/{nodeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteNode(@PathVariable Integer nodeId) throws Exception{
        if(mapNodeRepository.existsByNodeId(nodeId)) {
            mapLineRepository.deleteAllByNodeFirst_NodeId(nodeId);
            mapLineRepository.deleteAllByNodeSecond_NodeId(nodeId);
            MapNode node = mapNodeRepository.findByNodeId(nodeId);
            mapNodeRepository.delete(node);
            return "redirect:/admin/node/list/0";
        }
        throw new Exception();
    }
}
