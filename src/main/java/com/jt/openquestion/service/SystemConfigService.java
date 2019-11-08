package com.jt.openquestion.service;

import com.jt.openquestion.entity.SystemConfig;
import com.jt.openquestion.entity.SystemConfigRaw;
import com.jt.openquestion.mapper.SystemConfigMapper;
import com.jt.openquestion.utils.CollectionExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class SystemConfigService {
    private SystemConfigMapper configMapper;

    @Autowired
    public SystemConfigService(SystemConfigMapper configMapper){
        this.configMapper = configMapper;
    }

    public SystemConfig getSystemConfig(){
        List<SystemConfigRaw> systemConfig = this.configMapper.getSystemConfig();
        SystemConfig config = new SystemConfig();
        if(systemConfig == null || systemConfig.size() == 0){
            return config;
        }
        for(Field f : config.getClass().getDeclaredFields()){
            String fieldName = f.getName();
            Optional<SystemConfigRaw> systemConfigRaw = CollectionExtension.firstOrDefault(systemConfig, x -> x.getConfigName().equalsIgnoreCase(fieldName));
            if(systemConfigRaw.isPresent()){
                f.setAccessible(true);
                try {
                    f.set(systemConfig, systemConfigRaw.get().getConfigValue());
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return config;
    }
}
