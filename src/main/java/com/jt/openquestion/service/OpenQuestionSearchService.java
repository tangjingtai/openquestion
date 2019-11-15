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

@Component
public class OpenQuestionSearchService {

    @Autowired
    RestHighLevelClient restClient;

    /// <summary>
    /// 搜索题目
    /// </summary>
    /// <param name="request">搜索题目请求</param>
    /// <returns>符合条件的题目列表</returns>
    public List<LiteOpenQuestion> searchQuestionDetails(OpenQuestionSearchRequest request) throws IOException {
        ArrayList<String> indexNames = new ArrayList<>(request.getSourcePlatforms().length);
        for (int sourcePlatform : request.getSourcePlatforms()) {
            String indexName = LiteOpenQuestion.fetchIndexName(sourcePlatform, request.getCourseId());
            if(existsIndex(indexName)){
                indexNames.add(indexName);
            }
        }
        int[] labelUseTypes = getQuestionLabelUseTypes(request);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> filter = boolQueryBuilder.filter();
        if(labelUseTypes != null && labelUseTypes.length>0){
            // "filter":{
            //      "bool":{
            //          "should":[
            //              "terms":{"LabelUseType": [xx,xx,xx,xx]},
            //              "bool":{"must_not":{"exists":{"field":"LabelUseType"}}}
            //          ]
            //      }
            //  }
            BoolQueryBuilder innerBoolForShould = QueryBuilders.boolQuery();
            List<QueryBuilder> mustNot = innerBoolForShould.mustNot();
            mustNot.add(QueryBuilders.existsQuery("LabelUseType"));
            BoolQueryBuilder filterInnerBool =  QueryBuilders.boolQuery();
            List<QueryBuilder> should = filterInnerBool.should();
            should.add(QueryBuilders.termsQuery("LabelUseType",labelUseTypes));
            should.add(innerBoolForShould);
            filter.add(filterInnerBool);
        }
        List<QueryBuilder> must = boolQueryBuilder.must();
        MatchQueryBuilder match = QueryBuilders.matchQuery("Content", request.getKeyword())
                                .cutoffFrequency(0.01f).minimumShouldMatch("20%");
        must.add(match);
        // 打印查询语句
        System.out.println(boolQueryBuilder.toString());
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder).from(0).size(request.getNumberResults()).trackTotalHits(true);
        searchRequest.source(searchSourceBuilder).indices(indexNames.toArray(new String[indexNames.size()]));
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        ArrayList<LiteOpenQuestion> result = new ArrayList<>(request.getNumberResults());
        for (SearchHit hit : searchHits) {
            LiteOpenQuestion openQuestion = new LiteOpenQuestion();
            result.add(openQuestion);
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            openQuestion.setOpenQuestionId(sourceAsMap.get("OpenQuestionId") instanceof Long ? (Long)sourceAsMap.get("OpenQuestionId") : (Integer)sourceAsMap.get("OpenQuestionId"));
            openQuestion.setContent((String)sourceAsMap.get("Content"));
            openQuestion.setCourseId((int)sourceAsMap.get("CourseId"));
            openQuestion.setSourcePlatform((int)sourceAsMap.get("SourcePlatform"));
        }
        return result;
    }

    private Set<String> indexNames;
    /**
     * 判断ES中是否已经存在指定名称的索引
     * @param indexName 索引名称
     * @return  如果已经存在返回true，否则false
     */
    private boolean existsIndex(String indexName) {
        if (indexNames != null && indexNames.contains(indexName))
            return true;
        GetAliasesRequest request = new GetAliasesRequest();
        try {
            GetAliasesResponse alias = restClient.indices().getAlias(request, RequestOptions.DEFAULT);
            indexNames = alias.getAliases().keySet();
            return indexNames.contains(indexName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取题目可用于打标签的类型
     * <p>注意：ES中LabelUseType字段缺失或者值为-1（老数据）表示支持打知识点标签、题目难度标签</p>
     *
     * @param request ES搜题请求对象
     * @return 可用于打标签的题目类型集合
     */
    private int[] getQuestionLabelUseTypes(OpenQuestionSearchRequest request) {
        if (request.getCanKnowledgePointLabel() && request.getCanQuestionLevelLabel()) {
            return new int[]
                    {
                            -1,
                            OpenQuestionUseTypeEnum.KnowledgePointLabel.getValue() | OpenQuestionUseTypeEnum.QuestionLevelLabel.getValue()
                    };
        }
        if (request.getCanKnowledgePointLabel()) {
            return new int[]
                    {
                            -1,
                            OpenQuestionUseTypeEnum.KnowledgePointLabel.getValue(),
                            OpenQuestionUseTypeEnum.KnowledgePointLabel.getValue() | OpenQuestionUseTypeEnum.QuestionLevelLabel.getValue()
                    };
        }
        if (request.getCanQuestionLevelLabel()) {
            return new int[]
                    {
                            -1,
                            OpenQuestionUseTypeEnum.QuestionLevelLabel.getValue(),
                            OpenQuestionUseTypeEnum.KnowledgePointLabel.getValue() | OpenQuestionUseTypeEnum.QuestionLevelLabel.getValue()
                    };
        }
        return null;
    }
}
