package com.upstox.util;

import com.upstox.dto.SubscriptionPojo;
import com.upstox.dto.Tread;
import com.upstox.dto.TreadBar;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class OHLCUtil {

    public static final BlockingQueue<Tread> treadStore = new LinkedBlockingQueue<>();
    public static final Map<String,BlockingQueue<List<TreadBar>>> treadBar = new ConcurrentHashMap<>();
    public static final Map<String, CopyOnWriteArrayList<SubscriptionPojo>> subscriberList = new ConcurrentHashMap<>();
    public static final AtomicInteger barNum =new AtomicInteger();

    public static final Instant getTimeFromEpoc(final String longTime){

        long unix_seconds = (long) Long.parseUnsignedLong(longTime);
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(unix_seconds);
        System.out.println(longTime);
        Instant tempinstant = Instant.ofEpochMilli(unix_seconds / 1000000L);
        System.out.println(tempinstant.toString());

        return tempinstant;

    }

    private OHLCUtil(){}
}
