package com.example.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // Designate configuration file
@EnableJpaAuditing // JPA monitoring enabler
public class JpaConfig {

}
