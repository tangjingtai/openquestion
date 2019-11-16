package com.jt.openquestion.algorithm;

import com.jt.openquestion.entity.KeywordRecord;
import com.jt.openquestion.enums.SimilarityComparisonEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容比较器
 */
@Component
public class ContentComparator {

    /**
     * 与另外一道题目比较，获得两道题目的相似度；
     * 比较过程将两道题目掉转位置后再比对一次；
     * @param keywords 当前题目关键字集合
     * @param compareKeywords 比较的题目关键字集合
     * @param similarityComparisonType 相似度对比类型
     * @return 两道题目的相似度，[0, 1]之间
     */
    public double compareSimilarityPercentage(List<KeywordRecord> keywords, List<KeywordRecord>  compareKeywords
            , int similarityComparisonType)
    {
        ArrayList<Double> scores = new ArrayList<>(2);
        if ((similarityComparisonType & (int) SimilarityComparisonEnum.POSITIVE.getValue()) > 0)
        {
            Map<String, KeywordRecord> compareMap = compareKeywords.stream().collect(Collectors.toMap(KeywordRecord::getKeyword, x->x));
            scores.add(compareSimilarityPercentage(keywords, compareMap));
        }
        if ((similarityComparisonType & (int)SimilarityComparisonEnum.REVERSE.getValue()) > 0)
        {
            Map<String, KeywordRecord> compareMap = keywords.stream().collect(Collectors.toMap(KeywordRecord::getKeyword, x->x));
            scores.add(compareSimilarityPercentage(compareKeywords, compareMap));
        }
        return scores.size() > 0 ? scores.stream().min(Double::compare).get() : 0.0;
    }

    /**
     * 计算两份分词内容的相似度，返回结果[0, 1]之间
     * @param keywords 当前内容
     * @param compareKeywords 用于对比的内容
     * @return 内容相似度，[0, 1]之间
     */
    private double compareSimilarityPercentage(List<KeywordRecord> keywords, Map<String, KeywordRecord> compareKeywords){
        double baseWeight = keywords.stream().mapToDouble(KeywordRecord::fetchWeight).sum();
        double sumWeight = 0.0;
        for(KeywordRecord keyword : keywords){
            if(!compareKeywords.containsKey(keyword.getKeyword())){
                continue;
            }
            KeywordRecord compare = compareKeywords.get(keyword.getKeyword());
            double weight = Math.min(keyword.fetchWeight(), compare.fetchWeight());
            sumWeight += weight;
        }
        return sumWeight / baseWeight;
    }
}
