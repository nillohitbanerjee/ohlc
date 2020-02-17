package com.upstox.dao;

import com.upstox.dto.SubscriptionPojo;
import com.upstox.dto.Tread;
import com.upstox.dto.TreadBar;
import com.upstox.util.OHLCUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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

    public BlockingQueue<List<TreadBar>> getTradeBar(String symbol) throws InterruptedException {

        return OHLCUtil.treadBar.get(symbol);
    }

    public void setTradeBar(String symbol, List<TreadBar> tradeBars) throws InterruptedException {

        if(OHLCUtil.treadBar.get(symbol)==null){

            BlockingQueue<List<TreadBar>> treadBarsHolder = new LinkedBlockingQueue<>();
            treadBarsHolder.add(tradeBars);
            OHLCUtil.treadBar.put(symbol,treadBarsHolder);

        }
        else{
            OHLCUtil.treadBar.get(symbol).add(tradeBars);
        }
    }

    public CopyOnWriteArrayList<SubscriptionPojo> getSubscriber(String symbol) throws InterruptedException {

        return OHLCUtil.subscriberList.get(symbol);

    }

    public Map<String, CopyOnWriteArrayList<SubscriptionPojo>> getSubscribers() throws InterruptedException {

        return OHLCUtil.subscriberList;

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
