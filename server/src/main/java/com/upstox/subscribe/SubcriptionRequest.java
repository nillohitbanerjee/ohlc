package com.upstox.subscribe;

import com.upstox.dao.StoreTrade;
import com.upstox.dto.SubscriptionPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubcriptionRequest {

    @Autowired
    @Qualifier("storeTrade")
    StoreTrade storeTrade;

    @RequestMapping("/subscription")
    public boolean index(SubscriptionPojo subscriptionPojo) {

        try {
            storeTrade.setSubscriber(subscriptionPojo);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
