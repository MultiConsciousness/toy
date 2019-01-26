package com.walker.test.controller;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    @Test
    public void encryptPwd(){
        String str = new SimpleHash("md5", "1234","TTTTTT",2).toString();
        System.out.println(str);
    }
}