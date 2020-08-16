package com.my.experiment.demo.service.impl;

import com.my.experiment.demo.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public void badLoop(int count) {
        Map<String,Object> map = new ConcurrentHashMap<>();
        int i = 0;
        while (true){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   map.put(i + "",i);
               }
           }) .start();
        }
    }
}
