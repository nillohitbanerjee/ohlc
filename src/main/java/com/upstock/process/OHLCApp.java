package com.upstock.process;

import akka.actor.typed.ActorSystem;
import com.upstock.compute.ComputeTradeActor;
import com.upstock.compute.events.ComputeTrade;
import com.upstock.producce.CreateTradeInfoActor;
import com.upstock.producce.events.ReadTread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = " com.upstock.subscribe")
public class OHLCApp {

    public static void main(String args[]){


        SpringApplication.run(OHLCApp.class, args);
        final ActorSystem<ReadTread> greeterMain = ActorSystem.create(CreateTradeInfoActor.create(), "treadCreatorActor");
        final ActorSystem<ComputeTrade> calculator = ActorSystem.create(ComputeTradeActor.create(), "trealculate");
        //#actor-systema


        //#main-send-messages
        greeterMain.tell(new ReadTread("C:\\Users\\nillo\\Desktop\\upstock\\trades-data\\trades.json"));
        calculator.tell(new ComputeTrade("start Compute"));


    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}

