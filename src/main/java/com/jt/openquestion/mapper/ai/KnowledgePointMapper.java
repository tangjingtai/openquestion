package com.jt.openquestion.mapper.ai;

import com.jt.openquestion.entity.KnowledgePoint;
import com.jt.openquestion.entity.ThirdPartyKnowledgePoint;

import java.util.List;

public interface KnowledgePointMapper {
    List<KnowledgePoint> getKnowledgePoints();

    List<ThirdPartyKnowledgePoint> getThirdPartyKnowledgePoints(int platformId);
}
