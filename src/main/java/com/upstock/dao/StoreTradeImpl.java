package com.upstock.dao;

import com.upstock.dto.Tread;
import com.upstock.util.OHLCUtil;

public class StoreTradeImpl implements StoreTrade {

    public void saveTrade(Tread tread){

        OHLCUtil.treadStore.add(tread);
    }

    public Tread getTrade() throws InterruptedException {

        return OHLCUtil.treadStore.take();
    }

    public int increaseBarNum() throws InterruptedException {

       return  OHLCUtil.barNum.getAndIncrement();
    }

    public void resetBarNum() throws InterruptedException {

        OHLCUtil.barNum.set(0);
    }

    public void getBarNum() throws InterruptedException {

        OHLCUtil.barNum.get();
    }

    public void getTradeBar() throws InterruptedException {

        OHLCUtil.barNum.get();
    }

    public void setTradeBar() throws InterruptedException {

        OHLCUtil.barNum.get();
    }



}
