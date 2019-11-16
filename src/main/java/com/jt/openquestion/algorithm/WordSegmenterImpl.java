package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.KeywordRecord;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordSegmenterImpl implements WordSegmenter {
    private static Forest forest;

    static {
        try {
//            org.ansj.library.UserDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
            InputStream in = WordSegmenterImpl.class.getResourceAsStream("/library/userLibrary/userLibrary.dic");
            forest = Library.makeForest(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文本分词后的结果
     *
     * @param text 文本内容
     * @return 分词结果集合
     */
    @Override
    public List<KeywordRecord> seg(String text) {
        Result result = ToAnalysis.parse(text, forest);
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
