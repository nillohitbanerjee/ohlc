package com.upstock.util;

import com.upstock.dto.Tread;
import com.upstock.dto.TreadBar;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class OHLCUtil {

    public static final BlockingQueue<Tread> treadStore = new LinkedBlockingQueue<>();
    public static final Map<String,BlockingQueue<TreadBar>> treadBar = new ConcurrentHashMap<>();
    public static final AtomicInteger barNum =new AtomicInteger();
}
