package com.upstock.subscribe;

import com.upstock.dto.SubscriptionPojo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubcriptionRequest {


    @RequestMapping("/subscription")
    public boolean index(SubscriptionPojo subscriptionPojo) {

        return true;
    }
}
