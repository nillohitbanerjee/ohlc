package com.upstox.dao;

import com.upstox.dto.SubscriptionPojo;
import com.upstox.dto.Tread;
import com.upstox.dto.TreadBar;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public interface StoreTrade {

    public void saveTrade(Tread tread);

    public Tread getTrade() throws InterruptedException;

    public int increaseBarNum() throws InterruptedException;

    public void resetBarNum() throws InterruptedException;

    public int getBarNum() throws InterruptedException;

    public BlockingQueue<List<TreadBar>> getTradeBar(String symbol) throws InterruptedException;

    public void setTradeBar(String symbol, List<TreadBar> tradeBars) throws InterruptedException;

    public Tread pollTrade() throws InterruptedException;

    public Tread peekTrade() throws InterruptedException;

    public CopyOnWriteArrayList<SubscriptionPojo> getSubscriber(String symbol) throws InterruptedException;

    public void setSubscriber(SubscriptionPojo subscriber) throws InterruptedException;
    public Map<String, CopyOnWriteArrayList<SubscriptionPojo>> getSubscribers() throws InterruptedException;

}
