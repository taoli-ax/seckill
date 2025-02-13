package com.jiuzhang.seckill.Controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/say")
public class HelloWorld {
    @RequestMapping("{word}")
    public String sayHello(@PathVariable("word") String word) {
        return word;
    }
}
