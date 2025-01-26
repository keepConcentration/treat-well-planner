package com.world.planner.global.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

  @Value("${jasypt.encryptor.password}")
  private String encryptorPassword;

  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setPoolSize(4);
    encryptor.setPassword(encryptorPassword);
    encryptor.setAlgorithm("PBEWithMD5AndDES");
    encryptor.setIvGenerator(new RandomIvGenerator());
    encryptor.setSaltGenerator(new RandomSaltGenerator());
    return encryptor;
  }

}
