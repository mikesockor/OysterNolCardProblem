# OysterNolCardProblem

The Oyster Card Problem
You are required to model the following fare card system which is a limited version of
London’s Oyster card system. At the end of the test, you should be able to demonstrate a
user loading a card with ?30, and taking the following trips, and then viewing the balance.
- Tube Holborn to Earl’s Court
- 328 bus from Earl’s Court to Chelsea
- Tube Earl’s court to Hammersmith
Operation
When the user passes through the inward barrier at the station, their oyster card is charged
the maximum fare.
When they pass out of the barrier at the exit station, the fare is calculated and the maximum
fare transaction removed and replaced with the real transaction (in this way, if the user
doesn’t swipe out, they are charged the maximum fare).

All bus journeys are charged at the same price.
The system should favour the customer where more than one fare is possible for a given
journey. E.g. Holburn to Earl’s Court is charged at ?2.50.

used Java8, and Postgres 9.2 db
used Spring Boot, just for test puprose 
obviously in production we should use vanila java as much as we can

0) create db in postgres SQL={CREATE DATABASE adfg;}
1) in root dir run "mvn clean install"
2) in target dir run "java -jar adfg-1.0-SNAPSHOT-exec.jar"

application running on 9095 port 

implemented
 - controller layers (card and transaction mapping)
 - repository layers (both)
 - service layers (validation and perist)

interfaces are includes documentations

application.yml db settings jpa:
    hibernate:
      ddl-auto: create
which means table will clear at application startup, to avoid - change "create" to "update"

--------------------------------------POST examples:
--------------------------------------creation card 

post http://localhost:9095/card
```json
{
    "id": 6959144023113,
    "owner": "mikie",
    "balance": 30
}
```
responce
```json
{
    "id": 6959144023113,
    "owner": "mikie",
    "balance": 30,
    "checkInTime": null,
    "checkinStationEntity": null
}
```
--------------------------------------transaction checkin
post http://localhost:9095/transaction
```json
{
    "cardEntity": {
        "id": 6959144023113,
        "owner": "mikie",
        "balance": 30
    },
    "stationEntity": {
        "name": "jlt",
        "stationType": 2,
        "zone": 1,
        "agglomerationEntity": {
            "name": "dubai"
        }
    },
    "transactionType": 0
}
```
responce
```json
{
    "cardEntity": {
        "id": 6959144023113,
        "owner": "mikie",
        "balance": 26.8,
        "checkInTime": 1510986497562,
        "checkinStationEntity": {
            "name": "undefined",
            "stationType": null,
            "zone": 1,
            "agglomerationEntity": {
                "name": "dubai"
            },
            "first": true
        }
    },
    "stationEntity": {
        "name": "jlt",
        "stationType": "bus",
        "zone": 1,
        "agglomerationEntity": {
            "name": "dubai"
        },
        "first": true
    },
    "transactionType": "IN"
}
```
--------------------------------------transaction checkout
post http://localhost:9095/transaction
```json
{
    "cardEntity": {
        "id": 6959144023113,
        "owner": "mikie",
        "balance": 30
    },
    "stationEntity": {
        "name": "jlt",
        "stationType": 2,
        "zone": 1,
        "agglomerationEntity": {
            "name": "dubai"
        }
    },
    "transactionType": 1
}
```
responce
```json
{
    "cardEntity": {
        "id": 6959144023113,
        "owner": "mikie",
        "balance": 28.2,
        "checkInTime": null,
        "checkinStationEntity": null
    },
    "stationEntity": {
        "name": "jlt",
        "stationType": "bus",
        "zone": 1,
        "agglomerationEntity": {
            "name": "dubai"
        },
        "first": true
    },
    "transactionType": "OUT"
}
```
--------------------------------------transaction history with hours depth
http://localhost:9095/transaction?cardId=6959144023113&hours=3
```json
{
    "cardEntity": {
        "id": 6959144023113,
        "owner": "mikie",
        "balance": 28.2,
        "checkInTime": null,
        "checkinStationEntity": null
    },
    "cardReportData": [
        {
            "checkTime": 1510990827913,
            "name": "jlt",
            "stationType": "bus",
            "zone": 1,
            "agglomerationName": "dubai",
            "transactionType": "IN"
        },
        {
            "checkTime": 1510990831351,
            "name": "jlt",
            "stationType": "bus",
            "zone": 1,
            "agglomerationName": "dubai",
            "transactionType": "OUT"
        }
    ]
}
```