package com.jt.openquestion.mapper;

import com.jt.openquestion.entity.SystemConfigRaw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigMapper {
    List<SystemConfigRaw> getSystemConfig();
}
