package com.upstox.consume;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.upstox.consume.events.ProcessTread;
import com.upstox.dao.StoreTrade;
import com.upstox.dao.StoreTradeImpl;

public class ProcessTreadInfoActor extends AbstractBehavior<ProcessTread> {

    StoreTrade storeTrade ;
    public static Behavior<ProcessTread> create() {
        return Behaviors.setup(ProcessTreadInfoActor::new);
    }
    private ProcessTreadInfoActor(ActorContext<ProcessTread> context) {
        super(context);
        storeTrade= new StoreTradeImpl();
    }

    @Override
    public Receive<ProcessTread> createReceive() {
        return newReceiveBuilder().onMessage(ProcessTread.class, this::processBehavior).build();
    }

    private Behavior<ProcessTread> processBehavior(ProcessTread command) {
        getContext().getLog().info("Hello {}!", command.tread);
        storeTrade.saveTrade(command.tread);
        return this;
    }
}