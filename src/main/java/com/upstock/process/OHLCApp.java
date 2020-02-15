package com.upstock.process;

import akka.actor.typed.ActorSystem;
import com.upstock.compute.ComputeTradeActor;
import com.upstock.compute.events.ComputeTrade;
import com.upstock.producce.CreateTradeInfoActor;
import com.upstock.producce.events.ReadTread;

public class OHLCApp {

    public static void main(String args[]){

        final ActorSystem<ReadTread> greeterMain = ActorSystem.create(CreateTradeInfoActor.create(), "treadCreatorActor");
        final ActorSystem<ComputeTrade> calculator = ActorSystem.create(ComputeTradeActor.create(), "trealculate");
        //#actor-systema

        //#main-send-messages
        greeterMain.tell(new ReadTread("C:\\Users\\nillo\\Desktop\\upstock\\trades-data\\trades.json"));
        calculator.tell(new ComputeTrade("start Compute"));
    }
}
