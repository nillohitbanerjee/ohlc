package com.upstox.push;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.upstox.dao.StoreTrade;
import com.upstox.dao.StoreTradeImpl;
import com.upstox.push.event.PushTrade;
import org.springframework.web.client.RestTemplate;

public class PushTradeActor extends AbstractBehavior<PushTrade> {

    StoreTrade storeTrade;
    public static Behavior<PushTrade> create() {
        return Behaviors.setup(PushTradeActor::new);
    }

    private PushTradeActor(ActorContext<PushTrade> context)
    {
        super(context);
        storeTrade = new StoreTradeImpl();
    }

    @Override
    public Receive<PushTrade> createReceive() {
        return newReceiveBuilder().onMessage(PushTrade.class, this::pushingTrades).build();
    }

    private Behavior<PushTrade> pushingTrades(PushTrade command) {

        try {
            storeTrade.getSubscribers().forEach((x,y)->{
                try {
                    y.forEach(client->{
                        try{
                        while(storeTrade.getTradeBar(x).peek()!=null) {
                            RestTemplate t = new RestTemplate();
                            t.postForLocation("http://"+client.getHost()+":"+client.getPort()+"/push", storeTrade.getTradeBar(x).poll());
                        }}catch(Exception e){
                            e.printStackTrace();
                            }

                    }
                    );

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
}


