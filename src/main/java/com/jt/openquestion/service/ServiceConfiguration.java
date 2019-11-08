package com.jt.openquestion.service;

import com.jt.openquestion.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Autowired
    SystemConfigService configService;

    @Bean
    public SystemConfig systemConfig(){
        return configService.getSystemConfig();
    }
}
