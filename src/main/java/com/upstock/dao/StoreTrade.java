package com.upstock.dao;

import com.upstock.dto.Tread;

public interface StoreTrade {

    public void saveTrade(Tread tread);
    public Tread getTrade() throws InterruptedException;
    public int increaseBarNum() throws InterruptedException ;
    public void resetBarNum() throws InterruptedException ;
}
