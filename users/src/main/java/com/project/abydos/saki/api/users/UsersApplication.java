package com.project.abydos.saki.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * usersパッケージのSpring Boot起動クラス.
 */
@SpringBootApplication(scanBasePackages = "com.project.abydos.saki")
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }
}
