package com.jt.openquestion.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.jt.openquestion.config.properties.DataSourceProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = DruidConfiguration.PACKAGE, sqlSessionFactoryRef = "aiSqlSessionFactory")
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
public class DruidConfiguration {
    // 精确到 ai 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.jt.openquestion.mapper.ai";
    static final String MAPPER_LOCATION = "classpath:mapping/ai/*Mapper.xml";

//    @Value("${ai.datasource.url}")
//    private String url;
//
//    @Value("${ai.datasource.username}")
//    private String user;
//
//    @Value("${ai.datasource.password}")
//    private String password;
//
//    @Value("${ai.datasource.driverClassName}")
//    private String driverClass;

    @Bean
    @ConfigurationProperties(prefix = "ai.datasource")
    public DataSourceProperties aiDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSourceProperties oauth2DataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean(name = "aiDataSource")
    @Primary
    public DataSource aiDataSource() {
        DataSourceProperties dataSourceProperties = aiDataSourceProperties();

        return getDataSource(dataSourceProperties);
    }

    @Bean(name = "oauth2DataSource")
    public DataSource oauth2DataSource() {
        DataSourceProperties dataSourceProperties = oauth2DataSourceProperties();

        return getDataSource(dataSourceProperties);
    }

    private DataSource getDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        return dataSource;
    }

    @Bean(name = "aiTransactionManager")
    @Primary
    public DataSourceTransactionManager aiTransactionManager() {
        return new DataSourceTransactionManager(aiDataSource());
    }

    @Bean(name = "aiSqlSessionFactory")
    @Primary
    public SqlSessionFactory aiSqlSessionFactory(@Qualifier("aiDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean
    public ServletRegistrationBean startViewServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
//        servletRegistrationBean.addInitParameter("deny","127.0.0.1");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","123456");
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略过滤的格式
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
