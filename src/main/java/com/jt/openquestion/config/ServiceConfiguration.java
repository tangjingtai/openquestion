package com.jt.openquestion.config;

import com.jt.openquestion.entity.SystemConfig;
import com.jt.openquestion.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public SystemConfig systemConfig(@Autowired SystemConfigService configService){
        return configService.getSystemConfig();
    }
}
