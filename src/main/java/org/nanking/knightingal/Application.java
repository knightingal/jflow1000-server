package org.nanking.knightingal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author Knightingal
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.nanking.knightingal.dao.jpa")
@EntityScan("org.nanking.knightingal.bean")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
