package org.woodwhales.browser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    // 设置网站默认首页
    @GetMapping("/")
    public String defaultIndex() {
        return "index";
    }
    
    // 主页
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
