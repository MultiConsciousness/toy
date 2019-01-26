package com.walker.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewRouterController {

    @GetMapping(value="/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(){
        return "unauthorized";
    }

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/404")
    public String notFound(){
        return "404";
    }

    @RequestMapping("/500")
    public String internalError(){
        return "500";
    }

    @RequestMapping("/demo")
    public String demo(){
        return "demo";
    }
}
