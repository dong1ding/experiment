package com.my.experiment.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.experiment.demo.service.DemoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DemoService demoService;


    @RequestMapping("demo/testRequest")
    public String testRequest() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("hello","world");
        map.put("code",0);
        return objectMapper.writeValueAsString(map);
    }

    @RequestMapping("demo/badLoop/{count}")
    public String badLoop(@PathVariable int count) {
        demoService.badLoop(count);
        return "bad loop execute count:" + count;
    }

}
