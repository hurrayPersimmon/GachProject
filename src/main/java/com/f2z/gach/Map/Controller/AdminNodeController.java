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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@SessionAttributes
public class AdminNodeController {
    private final MapNodeRepository mapNodeRepository;

    @GetMapping("/node")
    public String nodeListPage(Model model){
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node-manage";
    }

    @GetMapping("/node/add")
    public String addNodePage(Model model){
        model.addAttribute("nodeDto", new MapDTO());
        return "node-add";
    }

    @PostMapping("/node")
    public String addNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapNodeDTO,
                          BindingResult result,
                          Model model){
        if(result.hasErrors()){
            return "node-add";
        }else if(mapNodeRepository.existsByNodeName(mapNodeDTO.toEntity().getNodeName())){
            model.addAttribute("errorMessage", "이미 존재하는 노드입니다.");
            return "node-add";
        }

        mapNodeRepository.save(mapNodeDTO.toEntity());
        model.addAttribute("message", "노드가 추가되었습니다.");
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node-manage";
    }

    @GetMapping("/node/{nodeId}")
    public String updateNodePage(@PathVariable Integer nodeId, Model model){
        MapNode mapNode = mapNodeRepository.findByNodeId(nodeId);
        model.addAttribute("nodeDto", mapNode);
        return "node-detail";
    }

    @PostMapping("/node/update")
    public String updateNode(@Valid @ModelAttribute("nodeDto") MapDTO.MapNodeDTO mapDTO,
                             BindingResult result, Model model){
        if(result.hasErrors()){
            return "node-detail";
        }else if(mapNodeRepository.existsByNodeName(mapDTO.toEntity().getNodeName()) &&
                !mapNodeRepository.findByNodeId(mapDTO.toEntity().getNodeId()).getNodeName().equals(mapDTO.toEntity().getNodeName())){
            model.addAttribute("errorMessage", "이미 존재하는 노드입니다.");
            return "node-detail";
        }
        log.info("{}", mapDTO.toEntity());
        MapNode mapNode = mapNodeRepository.findByNodeId(mapDTO.toEntity().getNodeId());
        mapNode.update(mapDTO.toEntity());
        mapNodeRepository.save(mapNode);
        model.addAttribute("message", "노드가 수정되었습니다.");
        model.addAttribute("nodeList", mapNodeRepository.findAll());
        return "node-manage";
    }

    @GetMapping("/node/delete/{nodeId}")
    public String deleteNode(@PathVariable Integer nodeId,
                             @ModelAttribute("nodeDto") MapDTO mapDTO,
                             Model model) throws Exception{
        if(mapNodeRepository.existsByNodeId(nodeId)) {
            MapNode node = mapNodeRepository.findByNodeId(nodeId);
            mapNodeRepository.delete(node);
            model.addAttribute("message", "노드가 삭제되었습니다.");
            model.addAttribute("nodeLIst", mapNodeRepository.findAll());
            return "redirect:/admin/node";
        }
        throw new Exception();
    }
}
