package com.csproject.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan
@EnableJpaRepositories(basePackages = "com.csproject.hrm.repositories")
@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class HumanResourceManagementSystemApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(HumanResourceManagementSystemApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(HumanResourceManagementSystemApplication.class);
  }
}