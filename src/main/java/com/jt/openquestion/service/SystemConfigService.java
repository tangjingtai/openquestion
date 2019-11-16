package com.jt.openquestion.service;

import com.jt.openquestion.entity.SystemConfig;
import com.jt.openquestion.entity.SystemConfigRaw;
import com.jt.openquestion.mapper.ai.SystemConfigMapper;
import com.jt.openquestion.utils.CollectionExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * 提供获取系统配置项的功能
 */
public interface SystemConfigService {

    /**
     * 获取系统配置对象
     * @return
     */
    SystemConfig getSystemConfig();
}
