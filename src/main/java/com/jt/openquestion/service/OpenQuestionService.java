package com.jt.openquestion.service;

import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.mapper.OpenQuestionMapper;
import com.lmash.mysqltest.generator.mapper.OpenquestionMapper;
import com.lmash.mysqltest.generator.pojo.Openquestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenQuestionService {

    @Autowired
    OpenQuestionMapper mapper;

    @Autowired
    OpenquestionMapper mapper2;

    public Long saveOpenQuestion(OpenQuestion openQuestion){
        mapper.saveOpenQuestion(openQuestion);
        return openQuestion.getOpenQuestionId();
    }

    public OpenQuestion getByOpenQuestionId(long id){
        List<OpenQuestion> openQuestions = getByOpenQuestionIds(new long[]{id});
        if(openQuestions == null || openQuestions.size() == 0){
            return null;
        }
        return openQuestions.get(0);
    }

    public Openquestion getById(long id){
        Openquestion openquestion = mapper2.selectByPrimaryKey(id);
        return openquestion;
    }

    public List<OpenQuestion> getByOpenQuestionIds(long[] ids){
        return mapper.getByOpenQuestionIds(ids);
    }
}
