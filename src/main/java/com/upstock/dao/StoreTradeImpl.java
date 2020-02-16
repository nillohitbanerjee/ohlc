package com.upstock.dao;

import com.upstock.dto.Tread;
import com.upstock.util.OHLCUtil;

import java.util.concurrent.TimeUnit;

public class StoreTradeImpl implements StoreTrade {

    public void saveTrade(Tread tread){

        OHLCUtil.treadStore.add(tread);
    }

    public Tread getTrade() throws InterruptedException {

        return OHLCUtil.treadStore.take();
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



}
