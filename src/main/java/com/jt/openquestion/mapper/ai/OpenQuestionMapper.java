package com.jt.openquestion.mapper.ai;

import com.jt.openquestion.entity.OpenQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OpenQuestionMapper {
    void saveOpenQuestion(OpenQuestion openQuestion);

    List<OpenQuestion> getByOpenQuestionIds(long[] ids);
}
