package com.upstox.rest;

import com.upstox.dto.SubscriptionPojo;
import com.upstox.dto.TreadBar;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UpStoxClientRestController {

    @PostMapping("push")
    public void index(@RequestBody List<TreadBar> treadBars) {

        treadBars.forEach(x->{
            System.out.println(x.toString());});


    }
    @GetMapping("ok")
    public String ok() {

        System.out.println("client is ok and running");;
        return "client is ok and running";


    }
}
