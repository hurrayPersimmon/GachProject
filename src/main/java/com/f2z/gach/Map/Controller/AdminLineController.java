package com.f2z.gach.Map.Controller;

import com.f2z.gach.DTO.Map.MapLineDTO;
import com.f2z.gach.DataGetter.dataDto;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.nodeEntity;
import com.f2z.gach.Map.DTO.MapDTO;
import com.f2z.gach.Map.Repository.MapLineRepository;
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
public class AdminLineController {
    private final MapLineRepository mapLineRepository;
    private final MapNodeRepository mapNodeRepository;

    @GetMapping("/line")
    public String lineListPage(Model model){
        model.addAttribute("lineList", mapLineRepository.findAll());
        return "line-manage";
    }

    @GetMapping("/line/add")
    public String addLinePage(Model model){
        model.addAttribute("nodeList", mapNodeRepository.findALLNodeName());
        return "line-add";
    }

    @PostMapping("/line")
    public String addLine(@Valid @ModelAttribute("lineDto") MapDTO.MapLineDTO mapLineDTO,
                          BindingResult result,
                          Model model){

        if(result.hasErrors()){
            return "line-add";
        }

        else if(mapLineRepository.existsByLineName(mapLineDTO.toEntity().getLineName()) &&
                !(mapLineRepository.findByLineId(mapLineDTO.toEntity().getLineId())
                        .getLineName().replaceAll(".$", "")
                        .equals(mapLineDTO.toEntity().getLineName()))) {
            //정규식 표현으로 마지막 문자 제거 (마지막 문자로 간선을 구분하기에)
            model.addAttribute("errorMessage", "이미 존재하는 이름입니다.");
            return "line-add";
        }

        //출발, 도착 노드 중복 확인
        else if(mapLineRepository.existsByNodeNameFirstAndNodeNameSecond(
                mapLineDTO.toEntity().getNodeNameFirst(),
                mapLineDTO.toEntity().getNodeNameSecond())){
            model.addAttribute("nameMessage", "이미 존재하는 경로입니다.");
            return "line-add";
        }

        // 출발, 도착 노드 같은 거 지정 했는지 확인
        else if(mapLineDTO.toEntity().getNodeNameFirst()
                .equals(mapLineDTO.toEntity().getNodeNameSecond())){
            model.addAttribute("pathMessage", "같은 경로를 지정하셨습니다.");
            return "line-add";
        }

        mapLineRepository.save(mapLineDTO.toSaveEntity("A",
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameFirst()),
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameSecond())));

        mapLineRepository.save(mapLineDTO.toSaveEntity("B",
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameSecond()),
                mapNodeRepository.findByNodeName(mapLineDTO.toEntity().getNodeNameFirst())));

        model.addAttribute("message", "간선이 추가되었습니다.");
        model.addAttribute("lineList", mapLineRepository.findAll());
        return "line-manage";
    }

    @GetMapping("/line/{lineId}")
    public String deleteLine(@PathVariable Integer lineId,
                             Model model)throws Exception{
        if(mapLineRepository.existsById(lineId)){
            mapLineRepository.deleteById(lineId);
            if(lineId%2==0) lineId-=1;
            else lineId+=1;
            mapLineRepository.deleteById(lineId);
            model.addAttribute("message", "간선이 삭제되었습니다.");
            model.addAttribute("lineList", mapLineRepository.findAll());
            return "line-manage";
        }
        throw new Exception();
    }





}
