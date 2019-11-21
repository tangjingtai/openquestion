package com.jt.openquestion.controller;

import com.jt.openquestion.controller.request.SimilarOpenQuestionRequest;
import com.jt.openquestion.entity.LiteOpenQuestion;
import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.entity.request.OpenQuestionSearchRequest;
import com.jt.openquestion.enums.SimilarityComparisonEnum;
import com.jt.openquestion.service.OpenQuestionSearchService;
import com.jt.openquestion.service.OpenQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class OpenQuestionController {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    OpenQuestionService openQuestionService;

    @Autowired
    OpenQuestionSearchService searchService;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/openquestion/{id}")
    public OpenQuestion openQuestion(@PathVariable("id") Long id){
        HttpSession session = this.request.getSession();
        session = this.request.getSession(true);
        String sessionId = session.getId();
        System.out.println("sessionId: "+ sessionId);
        OpenQuestion openQuestion = openQuestionService.getByOpenQuestionId(id);
        return openQuestion;
    }

    @GetMapping("/openquestion/search")
    public int search(){
        OpenQuestionSearchRequest request = new OpenQuestionSearchRequest();
        request.setKeyword("已知二次函数 ,若对任意 ,恒有 成立,不等式 的解集为 1 求集合 2 设集合 若集合 是集合 的子集,求 的取值范围");
        request.setCourseId(10);
        request.setSourcePlatforms(new int[]{2,3,4});
        request.setCanKnowledgePointLabel(true);
        request.setCanQuestionLevelLabel(true);
        try {
            List<LiteOpenQuestion> liteOpenQuestions = searchService.searchQuestionDetails(request);
            return liteOpenQuestions.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @PostMapping("/openquestion/similarQuestions")
    public List<OpenQuestion> similarOpenQuestions(@RequestBody SimilarOpenQuestionRequest request) throws IOException {
        logger.info("access /openquestion/similarQuestions .....");
        if(request == null)
            return null;
        if (request.getCourseId() == null || request.getCourseId() < 0)
            return null;
        if (request.getSimilarScoreThreshold() == null || request.getSimilarScoreThreshold() <= 0.0 || request.getSimilarScoreThreshold() > 1.0)
            return null;
        return openQuestionService.getSimilarOpenQuestions(request.getContent(), request.getCourseId(),
                request.getSimilarScoreThreshold(), SimilarityComparisonEnum.Overall);
    }
}
