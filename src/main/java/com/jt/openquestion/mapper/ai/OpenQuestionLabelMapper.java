package com.jt.openquestion.mapper.ai;

import com.jt.openquestion.entity.OpenQuestionLabel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OpenQuestionLabelMapper {
    /**
     * 根据OpenQuestionId获取对应的题目标签列表
     * @param openQuestionIds open题库Id集合
     * @return openQuestion的标签集合
     */
    List<OpenQuestionLabel> getQuestionLabelByIds(long[] openQuestionIds);
}
