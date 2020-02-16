package com.upstock.dao;

import com.upstock.dto.Tread;
import com.upstock.util.OHLCUtil;

public interface StoreTrade {

    public void saveTrade(Tread tread);
    public Tread getTrade() throws InterruptedException;
    public int increaseBarNum() throws InterruptedException ;
    public void resetBarNum() throws InterruptedException ;
    public int getBarNum() throws InterruptedException ;
    public void getTradeBar() throws InterruptedException ;
    public void setTradeBar() throws InterruptedException ;
    public Tread pollTrade() throws InterruptedException;
    public Tread peekTrade() throws InterruptedException ;
}
