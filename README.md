# Verticle Demo project


## Eclipse

Run main verticle on eclipse :

```
Main class : io.vertx.core.Launcher
Arguments : run ancm.training.vertx.MainVerticle

```


## Local Event bus Main

Run LocalEventBusMain

Test SSE (server side event) using HTTPie :
```
http http://localhost:8080/sse --stream 

```
Test application via browser : 
```
http://localhost:8080/

```

## Distributed Event bus :
Infinispan is used as the cluster manager

Run DistributedEventBusFirstInstance and DistributedEventBusSecondInstance

Test application via browser : 
```
http://localhost:8080/
http://localhost:8081/

```

## BeyondCallback
Launch BeyondCallBackMain

Test gateway service using HHTPie :
```
http :8080

```

## Logging

The project uses log4j2

## Links

https://github.com/jponge/vertx-in-action