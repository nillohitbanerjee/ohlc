package com.upstock.dao;

import com.upstock.dto.SubscriptionPojo;
import com.upstock.dto.Tread;
import com.upstock.util.OHLCUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component(value="storeTrade")
public class StoreTradeImpl implements StoreTrade {

    public void saveTrade(Tread tread){

        OHLCUtil.treadStore.add(tread);
    }

    public Tread getTrade() throws InterruptedException {

        return OHLCUtil.treadStore.poll(15,TimeUnit.SECONDS);
    }
    public Tread pollTrade() throws InterruptedException {

        return OHLCUtil.treadStore.poll(30, TimeUnit.SECONDS);
    }

    public Tread peekTrade() throws InterruptedException {

        return OHLCUtil.treadStore.peek();
    }

    public int increaseBarNum() throws InterruptedException {

       return  OHLCUtil.barNum.getAndIncrement();
    }

    public void resetBarNum() throws InterruptedException {

        OHLCUtil.barNum.set(0);
    }

    public int getBarNum() throws InterruptedException {

        return OHLCUtil.barNum.get();
    }

    public void getTradeBar() throws InterruptedException {

        OHLCUtil.barNum.get();
    }

    public void setTradeBar() throws InterruptedException {

        OHLCUtil.barNum.get();
    }

    public CopyOnWriteArrayList<SubscriptionPojo> getSubscriber(String symbol) throws InterruptedException {

        return OHLCUtil.subscriberList.get(symbol);

    }

    public void setSubscriber(SubscriptionPojo subscriber) throws InterruptedException {

        if(OHLCUtil.subscriberList.get(subscriber.getSymbol())==null){

            CopyOnWriteArrayList<SubscriptionPojo> sysbolSubscriber = new CopyOnWriteArrayList<>();
            sysbolSubscriber.add(subscriber);
            OHLCUtil.subscriberList.put(subscriber.getSymbol(),sysbolSubscriber);

        }
        else{
            OHLCUtil.subscriberList.get(subscriber.getSymbol()).add(subscriber);
        }
    }




}
