# OysterNolCardProblem

used Java8, and mongodb
just start and use

POST http://localhost:8090/card
```json
{
    "id": "6959144023113",
    "balance": 30
}
```
response 200 OK
```
[
  {
    "id": "6959144023113",
    "balance": 176.8,
    "checkInTime": null,
    "stationType": null,
    "stationZone": null
    "createdDate": null,
    "lastModifiedDate": "2018-04-28T14:00:49.423Z"
  }
]
```

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
[
  {
    "id": "5ae46ff680c17e01142eb75e",
    "checkInTime": "2018-04-28T12:58:30.591+0000",
    "type": "IN",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1,
    "cost": 173.60000000000002,
    "lastModifiedDate": "2018-04-28T14:00:49.423Z"
  }
]
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
[
  {
    "id": "5ae4702d80c17e01142eb75f",
    "checkInTime": "2018-04-28T12:59:25.856+0000",
    "type": "OUT",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1,
    "cost": 175.0,
    "lastModifiedDate": "2018-04-28T14:00:49.423Z"
  }
]
```
POST http://localhost:8090/transactions
```json
[
  {
    "t1": "6959144023113",
    "t2": "4"
  }
]
```
response 200 OK
```json
[
  {
    "id": "5ae46ff680c17e01142eb75e",
    "checkInTime": "2018-04-28T12:58:30.591+0000",
    "type": "IN",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1,
    "cost": 173.60000000000002
  },
  {
    "id": "5ae4702d80c17e01142eb75f",
    "checkInTime": "2018-04-28T12:59:25.856+0000",
    "type": "OUT",
    "cardId": "6959144023113",
    "stationName": "fgb",
    "stationType": "bus",
    "stationZone": 1,
    "cost": 175.0
  }
]
```
