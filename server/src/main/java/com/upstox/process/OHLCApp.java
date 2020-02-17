package com.upstox.process;

import akka.actor.typed.ActorSystem;
import com.upstox.producce.CreateTradeInfoActor;
import com.upstox.producce.events.ReadTread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = " com.upstox.subscribe , com.upstox.dao")
public class OHLCApp {

    public static void main(String args[]){

        SpringApplication.run(OHLCApp.class, args);


        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter something (start to start the server): ");
        String input = "";
        while (!"start".equalsIgnoreCase(input)) {



            if(scanner!=null) {
                input = scanner.nextLine();
                System.out.println("input : " + input);

                final ActorSystem<ReadTread> greeterMain = ActorSystem.create(CreateTradeInfoActor.create(), "treadCreatorActor");
                greeterMain.tell(new ReadTread(System.getProperty("filePath")));
                break;
            }
        }

        System.out.println("bye bye server started");
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

