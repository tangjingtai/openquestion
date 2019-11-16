package com.jt.openquestion.service;

import com.jt.openquestion.entity.LiteOpenQuestion;
import com.jt.openquestion.entity.request.OpenQuestionSearchRequest;
import com.jt.openquestion.enums.OpenQuestionUseTypeEnum;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface OpenQuestionSearchService {

    /// <summary>
    /// 搜索题目
    /// </summary>
    /// <param name="request">搜索题目请求</param>
    /// <returns>符合条件的题目列表</returns>
    List<LiteOpenQuestion> searchQuestionDetails(OpenQuestionSearchRequest request) throws IOException ;
}
