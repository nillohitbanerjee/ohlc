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
import com.upstock.dto.TreadBar;
import com.upstock.util.OHLCUtil;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        Map<String,List<List<TreadBar>>> op = new ConcurrentHashMap<>();
        List<TreadBar> fifteenSecondsBar = new ArrayList<>();
        List<List<TreadBar>> fifteenSecondsBars = new ArrayList<>();

        try {
            while (storeTrade.peekTrade()!=null) {

              Instant endTime = startingMoment.plusSeconds(15);
                Tread t = storeTrade.peekTrade();
                if(OHLCUtil.getTimeFromEpoc(t.getTs2()).isAfter(endTime)){
                    startingMoment =OHLCUtil.getTimeFromEpoc(t.getTs2()).plusSeconds(15);
                    endTime = startingMoment.plusSeconds(15);
                    storeTrade.increaseBarNum();
                }
                    if (t!=null) {
                    Instant tempinstant = OHLCUtil.getTimeFromEpoc(t.getTs2());
                    System.out.println(tempinstant.toString());
                    while (tempinstant.isBefore(endTime)) {

                        // treadBar
                        Tread compute = storeTrade.getTrade();

                        TreadBar treadBar = new TreadBar();
                        treadBar.setBar_num(storeTrade.getBarNum());
                        treadBar.setEvent("ohlc_notify");
                        treadBar.setSymbol(compute.getSym());
                        treadBar.setH(compute.getP());
                        treadBar.setC("0.0");
                        treadBar.setL(compute.getP());
                        treadBar.setO(compute.getP());
                        treadBar.setVolume(compute.getQ());
                        fifteenSecondsBar.add(treadBar);
                        //t = storeTrade.getTrade();
                        if(storeTrade.peekTrade()!=null) {
                            tempinstant = OHLCUtil.getTimeFromEpoc(storeTrade.peekTrade().getTs2());

                            if (tempinstant.isAfter(endTime)) {
                                fifteenSecondsBars.add(fifteenSecondsBar);
                                fifteenSecondsBar = new ArrayList<>();
                                startingMoment = tempinstant;
                                storeTrade.increaseBarNum();
                            }
                        }
                        else{
                            fifteenSecondsBars.add(fifteenSecondsBar);
                            System.out.println("processing done");
                            break;


                        }

                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("prepared");
        //#greeter-send-message
        //command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
        //#greeter-send-message
        return this;
    }

}