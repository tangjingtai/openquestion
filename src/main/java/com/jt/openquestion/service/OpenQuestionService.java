package com.jt.openquestion.service;

import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.mapper.OpenQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenQuestionService {

    @Autowired
    OpenQuestionMapper mapper;

    public Long saveOpenQuestion(OpenQuestion openQuestion){
        mapper.saveOpenQuestion(openQuestion);
        return openQuestion.getOpenQuestionId();
    }

    public List<OpenQuestion> getByOpenQuestionIds(long[] ids){
        return mapper.getByOpenQuestionIds(ids);
    }
}
