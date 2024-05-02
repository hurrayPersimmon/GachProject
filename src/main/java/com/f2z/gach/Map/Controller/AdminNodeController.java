package com.f2z.gach.Map.Controller;

import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminNodeController {
    private final MapNodeRepository mapNodeRepository;

    @GetMapping("/node")
    public String nodeListPage(Model model){
        List<MapNode> nodeList = mapNodeRepository.findAll();
        Collections.reverse(nodeList);
        model.addAttribute("nodeList", nodeList);
        return "node-manage";
    }

    @GetMapping("/node/add")
    public String addNodePage(Model model){
        model.addAttribute("nodeDto", new MapDTO.MapNodeDTO());
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node-add";
    }

    @PostMapping("/node")
    public String addNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapNodeDTO,
                          BindingResult result){
        if(result.hasErrors()){
            return "node-add";
        }
        mapNodeRepository.save(mapNodeDTO.toEntity());
        return "redirect:/admin/node";
    }

    @GetMapping("/node/{nodeId}")
    public String updateNodePage(@PathVariable Integer nodeId, Model model){
        MapNode mapNode = mapNodeRepository.findByNodeId(nodeId);
        model.addAttribute("nodeDto", mapNode);
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node-detail";
    }

    @PostMapping("/node/update")
    public String updateNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapDTO,
                             BindingResult result){
        if(result.hasErrors()){
            log.info("오류");
            return "node-detail";
        }
        MapNode mapNode = mapNodeRepository.findByNodeId(mapDTO.toEntity().getNodeId());
        mapNode.update(mapDTO.toEntity());
        mapNodeRepository.save(mapNode);
        return "redirect:/admin/node";
    }

    @GetMapping("/node/delete/{nodeId}")
    public String deleteNode(@PathVariable Integer nodeId) throws Exception{
        if(mapNodeRepository.existsByNodeId(nodeId)) {
            MapNode node = mapNodeRepository.findByNodeId(nodeId);
            mapNodeRepository.delete(node);
            return "redirect:/admin/node";
        }
        throw new Exception();
    }
}
