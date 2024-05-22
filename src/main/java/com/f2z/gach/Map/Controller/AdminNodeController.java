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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
    public String nodeListPage(Model model, @PathVariable Integer page){

        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.DESC,"nodeId").withPage(page);
        Page<MapNode> nodePage = mapNodeRepository.findAll(pageable);
        List<MapDTO.MapNodeListStructure> nodeList = nodePage.getContent().stream()
                .map(MapDTO.MapNodeListStructure::toMapNodeListStructure)
                .collect(Collectors.toList());
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
        // 불만족 노드 5개
        model.addAttribute("unSatisfaction", userHistoryRepository.findBottomMapNodes(5));
        model.addAttribute("lineRepo", mapLineRepository.findAll());
        model.addAttribute("countNodes", mapNodeRepository.countNodesNotInLines());
        return "node/node-manage";
    }

    @GetMapping("/node/sortedlist/{page}")
    public String nodeListSortedPage(Model model, @PathVariable Integer page, @RequestParam String sort){
        Pageable pageable = PageRequest.ofSize(10).withSort(Sort.Direction.ASC, "nodeId").withPage(page);
        Page<MapNode> nodePage = mapNodeRepository.findAllByNodeNameContaining(sort, pageable);
        List<MapDTO.MapNodeListStructure> nodeList = nodePage.getContent().stream()
                .map(MapDTO.MapNodeListStructure::toMapNodeListStructure)
                .collect(Collectors.toList());
        model.addAttribute("nodeList", MapDTO.toMapNodeList(nodePage, nodeList));
        model.addAttribute("nodeChartData", mapNodeRepository.findAll());
        model.addAttribute("lineRepo", mapLineRepository.findAll());
        model.addAttribute("countNodes", mapNodeRepository.countNodesNotInLines());
        model.addAttribute("inquiryList", inquiryRepository.findAllByCreateDtBetween(LocalDateTime.now().minusWeeks(1), LocalDateTime.now()));
        model.addAttribute("inquiryNodeList", inquiryRepository.findAllByCreateDtBetweenAndInquiryCategory(LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), InquiryCategory.Node));

        return "node/node-manage";
    }

    @GetMapping("/node/add")
    public String addNodePage(Model model){
        model.addAttribute("nodeDto", new MapDTO.MapNodeDTO());
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node/node-add";
    }

    @PostMapping("/node")
    @PreAuthorize("hasRole('ADMIN')")
    public String addNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapNodeDTO,
                          BindingResult result){
        if(result.hasErrors()){
            return "node/node-add";
        }
        mapNodeRepository.save(mapNodeDTO.toEntity());
        return "redirect:/admin/node/list/0";
    }

    @GetMapping("/node/{nodeId}")
    public String updateNodePage(@PathVariable Integer nodeId, Model model){
        MapNode mapNode = mapNodeRepository.findByNodeId(nodeId);
        model.addAttribute("nodeDto", mapNode);
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node/node-detail";
    }

    @PostMapping("/node/update")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
