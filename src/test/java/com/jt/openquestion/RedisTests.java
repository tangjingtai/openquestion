package com.jt.openquestion;

import com.jt.openquestion.entity.KeywordRecord;
import com.jt.openquestion.entity.KnowledgePoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@SpringBootTest
public class RedisTests {

    @Autowired
    RedisTemplate<String, KnowledgePoint> knowledgeRedisTemplate;

    @Autowired
    RedisTemplate<String, KeywordRecord> keywordRecordRedisTemplate;

    @Autowired
    RedisTemplate stringRedisTemplate;

    @Test
    public void testKnowledge(){

        KnowledgePoint k1 = new KnowledgePoint();
        k1.setCourseId(1);
        k1.setKnowledgePointId(1);
        k1.setKnowledgePointName("k1");

        KnowledgePoint k2 = new KnowledgePoint();
        k2.setCourseId(2);
        k2.setKnowledgePointId(2);
        k2.setKnowledgePointName("k2");

        String key = "KnowledgePoint_Test_Key";
        knowledgeRedisTemplate.opsForSet().add(key, k1,k2);
        Set<KnowledgePoint> result = knowledgeRedisTemplate.opsForSet().members(key);
        System.out.println(result);


        KeywordRecord keywordRecord = new KeywordRecord("中国", 2);
        key = "KeywordRecord_Test_Key";
        keywordRecordRedisTemplate.opsForValue().set(key, keywordRecord);
        keywordRecord =keywordRecordRedisTemplate.opsForValue().get(key);
        System.out.println(keywordRecord);
    }

    @Test
    public void testKeywordRecord(){
        KeywordRecord keywordRecord = new KeywordRecord("中国", 2);
        String key = "KeywordRecord_Test_Key";
        keywordRecordRedisTemplate.opsForValue().set(key, keywordRecord);
        keywordRecord =keywordRecordRedisTemplate.opsForValue().get(key);
        System.out.println(keywordRecord);
    }
}
