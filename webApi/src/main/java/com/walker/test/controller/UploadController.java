package com.walker.test.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/upload")
public class UploadController {

    @RequiresPermissions("query:item")
    @RequestMapping("/testUpload")
    public String testKickOut(){
        System.out.println("=======================================访问测试controller========================================");
        return "forward:/view/home";
    }
}
