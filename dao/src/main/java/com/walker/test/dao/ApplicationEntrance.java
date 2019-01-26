package com.walker.test.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.walker.test.dao.mapper.UserMapper")
public class ApplicationEntrance {
    public static void main(String[] args){
        SpringApplication.run(ApplicationEntrance.class,args);
    }
}
