package com.jt.openquestion.controller;

import com.jt.openquestion.entity.LiteOpenQuestion;
import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.entity.request.OpenQuestionSearchRequest;
import com.jt.openquestion.service.OpenQuestionSearchService;
import com.jt.openquestion.service.OpenQuestionService;
import com.lmash.mysqltest.generator.pojo.Openquestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class OpenQuestionController {

    @Autowired
    OpenQuestionService service;

    @Autowired
    OpenQuestionSearchService searchService;

    @GetMapping("/openquestion/{id}")
    public OpenQuestion openQuestion(@PathVariable("id") Long id){
        OpenQuestion openQuestion = service.getByOpenQuestionId(id);
        return openQuestion;
    }

    @GetMapping("/openquestion2/{id}")
    public Openquestion openQuestion2(@PathVariable("id") Long id){
        Openquestion openQuestion = service.getById(id);
        return openQuestion;
    }

    @GetMapping("/openquestion/search")
    public int search(){
        OpenQuestionSearchRequest request = new OpenQuestionSearchRequest();
        request.setKeyword("已知二次函数 ,若对任意 ,恒有 成立,不等式 的解集为 1 求集合 2 设集合 若集合 是集合 的子集,求 的取值范围");
        request.setCourseId(10);
        request.setSourcePlatforms(new int[]{2,3,4});
        request.setCanKnowledgePointLabel(true);
//        request.setCanQuestionLevelLabel(true);
        try {
            List<LiteOpenQuestion> liteOpenQuestions = searchService.searchQuestionDetails(request);
            return liteOpenQuestions.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
