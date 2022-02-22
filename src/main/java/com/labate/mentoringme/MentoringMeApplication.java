package com.labate.mentoringme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.labate.mentoringme")
@EnableJpaAuditing
@EnableJpaRepositories
@EnableTransactionManagement
public class MentoringMeApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(MentoringMeApplication.class, args);
  }
}
