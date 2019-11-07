package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.KeywordRecord;

import java.util.List;

public interface WordSegmenter {
    /**
     * 获取文本分词后的结果
     * @param text 文本内容
     * @return 分词结果集合
     */
    List<KeywordRecord> seg(String text);
}
