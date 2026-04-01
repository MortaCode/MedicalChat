package org.myy.medicalchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.myy.medicalchat.**.mapper")
public class MedicalChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalChatApplication.class, args);
    }

}
