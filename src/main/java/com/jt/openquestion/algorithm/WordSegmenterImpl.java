package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.KeywordRecord;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordSegmenterImpl implements WordSegmenter {
    /**
     * 获取文本分词后的结果
     *
     * @param text 文本内容
     * @return 分词结果集合
     */
    @Override
    public List<KeywordRecord> seg(String text) {
        Result result = ToAnalysis.parse(text);
        Map<String, KeywordRecord> map = new HashMap<>(result.size());
        for(Term term : result.getTerms()){
            String name = term.getName();
            if(!map.containsKey(name)){
                map.put(name, new KeywordRecord(name));
            }else{
                map.get(name).increment();
            }
        }

        return new ArrayList<>(map.values());
    }
}
