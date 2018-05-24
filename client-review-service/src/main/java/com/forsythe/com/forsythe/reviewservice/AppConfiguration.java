package com.forsythe.com.forsythe.reviewservice;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/flowable")
                .username("flowable")
                .password("flowable")
                .build();
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean
//    public ProcessEngineConfiguration processEngineConfiguration(PlatformTransactionManager transactionManager) {
//        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
//        processEngineConfiguration.setDeploymentResources(new Resource[]{new ClassPathResource("client-review-process.xml")});
//        processEngineConfiguration.setTransactionManager(transactionManager);
//        return processEngineConfiguration;
//    }
//
//    @Bean
//    public ProcessEngineFactoryBean processEngineFactoryBean(ProcessEngineConfiguration processEngineConfiguration) {
//        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
//        factoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
//        return factoryBean;
//    }

//    @Bean
//    public Deployment deployment() {
//        return repositoryService.createDeployment()
//                .addClasspathResource("org/flowable/spring/test/hello.bpmn20.xml").deploy();
//    }
}
