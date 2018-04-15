# OysterNolCardProblem

used Java8, and mongodb
just start and use

POST http://localhost:8090/card
```json
{
    "id": "6959144023113",
    "owner": "mikie",
    "balance": 30
}
```
response 200 OK


POST http://localhost:8090/transaction
```json
{
    "type": "IN",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1
}
```
response 200 OK
```json
{
    "message": null,
    "cost": 30
}
```
POST http://localhost:8090/transaction
```json
{
    "type": "OUT",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1
}
```
response 200 OK
```json
{
    "message": null,
    "cost": 28.2
}
```
http://localhost:8090/transaction?cardId=6959144023113&hours=4
```json
[
    {
        "id": "5a1528c185a42d1c60cf8657",
        "checkInTime": 1511336129221,
        "type": "IN",
        "cardId": "6959144023113",
        "stationName": "fgb",
        "stationType": "bus",
        "stationZone": 1,
        "cost": 0
    },
    {
        "id": "5a1528d285a42d1c60cf8658",
        "checkInTime": 1511336146735,
        "type": "OUT",
        "cardId": "6959144023113",
        "stationName": "fgb",
        "stationType": "bus",
        "stationZone": 1,
        "cost": 0
    }
]
```

http://www.baeldung.com/spring-5-webclient
From a Mono:

return customerMono
           .flatMap(c -> ok().body(BodyInserters.fromObject(c)))
           .switchIfEmpty(notFound().build());
From a Flux:

return customerFlux
           .collectList()
           .flatMap(l -> {
               if(l.isEmpty()) {
                 return notFound().build();

               }
               else {
                 return ok().body(BodyInserters.fromObject(l)));
               }
           });