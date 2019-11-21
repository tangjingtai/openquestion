package com.jt.openquestion.service;

import com.jt.openquestion.entity.SystemConfig;

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
