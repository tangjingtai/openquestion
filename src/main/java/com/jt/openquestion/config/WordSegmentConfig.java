package com.jt.openquestion.config;

import com.jt.openquestion.algorithm.WordSegmenter;
import com.jt.openquestion.algorithm.WordSegmenterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WordSegmentConfig {

    @Bean
    public WordSegmenter wordSegmenter(){
        WordSegmenter seg = WordSegmenterImpl.build();
        Thread t= new Thread(()->{
            seg.seg("this is warmup.");
            System.out.println("ansj warmup complete.");
        });
        t.start();

        return seg;
    }
}
