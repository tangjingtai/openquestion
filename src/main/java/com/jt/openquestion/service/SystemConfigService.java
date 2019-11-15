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
        List<SystemConfigRaw> systemConfigRaws = this.configMapper.getSystemConfig();
        SystemConfig config = new SystemConfig();
        if(systemConfigRaws == null || systemConfigRaws.size() == 0){
            return config;
        }
        for(Field f : config.getClass().getDeclaredFields()){
            String fieldName = f.getName();
            Optional<SystemConfigRaw> systemConfigRaw = CollectionExtension.firstOrDefault(systemConfigRaws, x -> x.getConfigName().equalsIgnoreCase(fieldName));
            if(systemConfigRaw.isPresent()){
                f.setAccessible(true);
                Class<?> fieldType = f.getType();
                try {
                    String val = systemConfigRaw.get().getConfigValue();
                    if(fieldType.equals(String.class)){
                        f.set(config, val);
                    }
                    if(fieldType.equals(Integer.class)){
                        f.set(config, Integer.parseInt(val));
                    }
                    if(fieldType.equals(Boolean.class)){
                        f.set(config, val.equals("1") || val.equalsIgnoreCase("true"));
                    }
                    if(fieldType.equals(Double.class)){
                        f.set(config, Double.parseDouble(val));
                    }
                    if(fieldType.equals(Float.class)){
                        f.set(config, Float.parseFloat(val));
                    }
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return config;
    }
}
