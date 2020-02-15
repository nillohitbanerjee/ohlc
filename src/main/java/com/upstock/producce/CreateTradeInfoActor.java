package com.upstock.producce;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upstock.consume.ProcessTreadInfoActor;
import com.upstock.consume.events.ProcessTread;
import com.upstock.dto.Tread;
import com.upstock.producce.events.ReadTread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CreateTradeInfoActor extends AbstractBehavior<ReadTread> {


    private final ActorRef<ProcessTread> treadProcessor;

    public static Behavior<ReadTread> create() {
        return Behaviors.setup(CreateTradeInfoActor::new);
    }

    private CreateTradeInfoActor(ActorContext<ReadTread> context) {
        super(context);
        //#create-actors
        treadProcessor = context.spawn(ProcessTreadInfoActor.create(), "treadProcessor");
        //#create-actors
    }

    @Override
    public Receive<ReadTread> createReceive() {
        return newReceiveBuilder().onMessage(ReadTread.class, this::processFile).build();
    }

    private Behavior<ReadTread> processFile(ReadTread command) {
        //#create-actors
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (Stream<String> stream = Files.lines(Paths.get(command.filePath))) {

            stream.forEach(input->{
                try {
                   // System.out.println(input);
                    Tread obj = objectMapper.readValue(input, Tread.class);
                    treadProcessor.tell(new ProcessTread(obj));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        //#create-actors
        return this;
    }
}

