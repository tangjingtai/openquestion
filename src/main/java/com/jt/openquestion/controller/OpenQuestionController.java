package com.jt.openquestion.controller;

import com.jt.openquestion.entity.OpenQuestion;
import com.jt.openquestion.service.OpenQuestionService;
import com.lmash.mysqltest.generator.pojo.Openquestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenQuestionController {

    @Autowired
    OpenQuestionService service;

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

}
