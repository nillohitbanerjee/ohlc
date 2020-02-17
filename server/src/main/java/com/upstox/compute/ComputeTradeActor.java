package com.upstox.compute;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.upstox.compute.events.ComputeTrade;
import com.upstox.dao.StoreTrade;
import com.upstox.dao.StoreTradeImpl;
import com.upstox.dto.Tread;
import com.upstox.dto.TreadBar;
import com.upstox.push.PushTradeActor;
import com.upstox.push.event.PushTrade;
import com.upstox.util.OHLCUtil;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeTradeActor extends AbstractBehavior<ComputeTrade> {

    StoreTrade storeTrade;
    Instant startingMoment;
    private final ActorRef<PushTrade> push;

    public static Behavior<ComputeTrade> create() {
        return Behaviors.setup(ComputeTradeActor::new);
    }

    private ComputeTradeActor(ActorContext<ComputeTrade> context) {
        super(context);
        push = context.spawn(PushTradeActor.create(), "treadPush");
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

                                Map <String , List <TreadBar >> tempCalculation = new HashMap<>();

                                fifteenSecondsBar.forEach(tr->{

                                    if(tempCalculation.get(tr.getSymbol())==null){

                                        List <TreadBar > tempList = new ArrayList<>();
                                        tempList.add(tr);
                                        tempCalculation.put(tr.getSymbol(),tempList);

                                    }
                                    else{
                                        tempCalculation.get(tr.getSymbol()).add(tr);
                                    }
                                });
                                tempCalculation.forEach((x,y)->{
                                    try {
                                        Double sum = y.stream()
                                                .map(tempTreadBar -> Double.parseDouble(tempTreadBar.getVolume()))
                                                .reduce(0.0, (a, b) -> a + b);

                                        int size = y.size();
                                        y.get(size-1).setVolume(sum+"");
                                        y.get(size-1).setC(y.get(size-1).getH());

                                        storeTrade.setTradeBar(x, y);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                });
                                fifteenSecondsBar = new ArrayList<>();
                                startingMoment = tempinstant;
                                storeTrade.increaseBarNum();
                                push.tell(new PushTrade("start pushing to client"));
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