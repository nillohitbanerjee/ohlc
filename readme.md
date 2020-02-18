# Analytical Server "OHLC" and Client

Create an Analytical Server "OHLC" (Open/High/Low/Close) time series based on the 'Trades' input dataset. The ‘Trades’ dataset is available at http://kaboom.rksv.net/trades-test/trades-data.zip.

Also we created a consumer or subscriber along with it


# Tech stack used

 1. Akka
 2. Spring boot
 3. Maven

## Server and Client Design

![server design](image/Picture%201.png)

## Build Application

Please use mvn clean install to build this maven project

## Run the application

java -jar <<"clientjar">> to run client jar 

then please hit [http://localhost:8081/ok](http://localhost:8081/ok) to check whether client is running 

java -jar <<<"serverjar">>> -DfilePath=<<"full path of json input">>

Then hit [http://localhost:8080/subscription?event=xyz&Symbol=XETHZUSD&interval=15&host=localhost&port=8081](http://localhost:8080/subscription?event=xyz&Symbol=XETHZUSD&interval=15&host=localhost&port=8081) 

to register subscriber

Then please enter <<start>> command to start the application