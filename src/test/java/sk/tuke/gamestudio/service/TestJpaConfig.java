package sk.tuke.gamestudio.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EntityScan("sk.tuke.gamestudio.entity")
public class TestJpaConfig {
}