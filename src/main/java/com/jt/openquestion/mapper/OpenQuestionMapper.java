package com.jt.openquestion.mapper;

import com.jt.openquestion.entity.OpenQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenQuestionMapper {
    List<OpenQuestion> getByOpenQuestionIds(long[] ids);
}
