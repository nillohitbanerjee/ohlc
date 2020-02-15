package com.upstock.compute;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.upstock.compute.events.ComputeTrade;
import com.upstock.dao.StoreTrade;
import com.upstock.dao.StoreTradeImpl;
import com.upstock.dto.Tread;
import com.upstock.util.OHLCUtil;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ComputeTradeActor extends AbstractBehavior<ComputeTrade> {

    StoreTrade storeTrade;
    Instant startingMoment;
    public static Behavior<ComputeTrade> create() {
        return Behaviors.setup(ComputeTradeActor::new);
    }

    private ComputeTradeActor(ActorContext<ComputeTrade> context) {
        super(context);
        storeTrade = new StoreTradeImpl();
    }

    @Override
    public Receive<ComputeTrade> createReceive() {
        while (true) {
            Tread o = OHLCUtil.treadStore.peek();
            if(o == null) continue;
            else{
                long unix_seconds = (long) Long.parseUnsignedLong(o.getTs2());
                final DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(unix_seconds);
                System.out.println(o.getTs2());
                Instant instant = Instant.ofEpochMilli(unix_seconds / 1000000L);
                System.out.println(instant.toString());
                startingMoment=instant;
                break;
            }
            // omitted for the sake of brevity
        }
        return newReceiveBuilder().onMessage(ComputeTrade.class, this::processBehavior).build();
    }

    private Behavior<ComputeTrade> processBehavior(ComputeTrade command) {
        getContext().getLog().info("Hello {}!", command.startMessage);
        try {
            while (true) {

                Instant endTime = startingMoment.plusSeconds(15);
                Tread t =storeTrade.getTrade();
                long unix_seconds = (long) Long.parseUnsignedLong(t.getTs2());
                final DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(unix_seconds);
                System.out.println(t.getTs2());
                Instant tempinstant = Instant.ofEpochMilli(unix_seconds / 1000000L);
                System.out.println(tempinstant.toString());
                while(tempinstant.isBefore(endTime)){

                   // treadBar
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //#greeter-send-message
        //command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
        //#greeter-send-message
        return this;
    }

}