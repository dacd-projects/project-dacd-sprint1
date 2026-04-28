# Space Weather & Aviation Analysis — Sprint 2

## Contexto

Proyecto que analiza el impacto del **clima espacial** en la **aviación comercial**, especialmente en rutas transpolares. Se capturan datos geomagnéticos (NOAA API) y vuelos en tiempo real (OpenSky API), publicándolos en un broker de mensajería (ActiveMQ) y almacenándolos en un Event Store basado en ficheros.

> Sprint 2: implementación del patrón Publisher/Subscriber mediante ActiveMQ y Event Store Builder.

---

## Módulos

| Módulo | Responsabilidad |
|---|---|
| `flight-api` | Captura vuelos en tiempo real desde OpenSky Network y los publica en ActiveMQ |
| `weather-api` | Captura índices Kp desde NOAA y los publica en ActiveMQ |
| `event-store-builder` | Se suscribe a los topics y persiste los eventos en un Event Store basado en ficheros |

---

## Arquitectura

### Diagrama de paquetes

```mermaid
graph TD
    subgraph flight-api
        FA_Main[Main] --> FA_Controller[FlightController]
        FA_Controller --> FA_Feeder[FlightFeeder]
        FA_Controller --> FA_Store[FlightStore]
        FA_Feeder --> FA_OpenSky[OpenSkyFlightFeeder]
        FA_Store --> FA_Sqlite[SqliteFlightStore]
        FA_Store --> FA_ActiveMQ[ActiveMQFlightStore]
        FA_OpenSky --> FA_Model[Flight]
        FA_Sqlite --> FA_Model
        FA_ActiveMQ --> FA_Model
    end

    subgraph weather-api
        WA_Main[Main] --> WA_Controller[SpaceWeatherController]
        WA_Controller --> WA_Feeder[NasaFeeder]
        WA_Controller --> WA_Store[SpaceWeatherStore]
        WA_Feeder --> WA_Client[SpaceWeatherClient]
        WA_Store --> WA_Sqlite[SqliteSpaceWeatherStore]
        WA_Store --> WA_ActiveMQ[ActiveMQSpaceWeatherStore]
        WA_Client --> WA_Model[SpaceWeather]
        WA_Sqlite --> WA_Model
        WA_ActiveMQ --> WA_Model
    end

    subgraph event-store-builder
        ESB_Main[Main] --> ESB_Builder[EventStoreBuilder]
        ESB_Builder --> ESB_FileStore[FileEventStore]
    end

    FA_ActiveMQ -- Topic: Flight --> ESB_Builder
    WA_ActiveMQ -- Topic: SpaceWeather --> ESB_Builder
```

### Diagrama de clases — `flight-api`

```mermaid
classDiagram
    class Main {
        +main(args: String[])
    }
    class FlightController {
        -feeder: FlightFeeder
        -store: FlightStore
        +run()
        +execute()
    }
    class FlightFeeder {
        <<interface>>
        +fetchFlights() List~Flight~
    }
    class FlightStore {
        <<interface>>
        +save(flight: Flight)
    }
    class OpenSkyFlightFeeder {
        +fetchFlights() List~Flight~
    }
    class SqliteFlightStore {
        +save(flight: Flight)
    }
    class ActiveMQFlightStore {
        +save(flight: Flight)
    }
    class Flight {
        -icao: String
        -callsign: String
        -country: String
        -latitude: double
        -longitude: double
        -altitude: double
        -velocity: double
        -lastUpdate: Instant
        -capturedAt: long
    }

    Main --> FlightController
    FlightController --> FlightFeeder
    FlightController --> FlightStore
    FlightFeeder <|.. OpenSkyFlightFeeder
    FlightStore <|.. SqliteFlightStore
    FlightStore <|.. ActiveMQFlightStore
    OpenSkyFlightFeeder --> Flight
    SqliteFlightStore --> Flight
    ActiveMQFlightStore --> Flight
```

### Diagrama de clases — `weather-api`

```mermaid
classDiagram
    class Main {
        +main(args: String[])
    }
    class SpaceWeatherController {
        -feeder: NasaFeeder
        -store: SpaceWeatherStore
        +run()
        +execute()
    }
    class NasaFeeder {
        <<interface>>
        +fetchEvents() List~SpaceWeather~
    }
    class SpaceWeatherStore {
        <<interface>>
        +save(event: SpaceWeather)
    }
    class SpaceWeatherClient {
        +fetchEvents() List~SpaceWeather~
    }
    class SqliteSpaceWeatherStore {
        +save(event: SpaceWeather)
    }
    class ActiveMQSpaceWeatherStore {
        +save(event: SpaceWeather)
    }
    class SpaceWeather {
        -eventType: String
        -kpIndex: double
        -startTime: String
        -endTime: String
        -source: String
        -capturedAt: long
    }

    Main --> SpaceWeatherController
    SpaceWeatherController --> NasaFeeder
    SpaceWeatherController --> SpaceWeatherStore
    NasaFeeder <|.. SpaceWeatherClient
    SpaceWeatherStore <|.. SqliteSpaceWeatherStore
    SpaceWeatherStore <|.. ActiveMQSpaceWeatherStore
    SpaceWeatherClient --> SpaceWeather
    SqliteSpaceWeatherStore --> SpaceWeather
    ActiveMQSpaceWeatherStore --> SpaceWeather
```

### Diagrama de clases — `event-store-builder`

```mermaid
classDiagram
    class Main {
        +main(args: String[])
    }
    class EventStoreBuilder {
        -brokerUrl: String
        -clientId: String
        -topics: String[]
        -fileEventStore: FileEventStore
        +start()
        +onMessage(message: Message)
    }
    class FileEventStore {
        +save(topic: String, json: String)
    }

    Main --> EventStoreBuilder
    EventStoreBuilder --> FileEventStore
```

---

## Event Store

Los eventos se almacenan en la siguiente estructura de directorios:

eventstore/
└── {topic}/
└── {source}/
└── {YYYYMMDD}.events

Ejemplo:

eventstore/
├── Flight/
│   └── flight-api/
│       └── 20260427.events
└── SpaceWeather/
└── weather-api/
└── 20260427.events

Cada línea de un fichero `.events` representa un evento JSON con al menos los campos:

- `ts` → timestamp del evento
- `ss` → identificador del sistema origen



## Compilar y ejecutar

### Compilar el proyecto completo

```bash
mvn install
```

### Ejecutar módulo de vuelos

```bash
cd flight-api
mvn exec:java -Dexec.mainClass="org.ulpgc.dacd.Main"
```

### Ejecutar módulo de clima espacial

```bash
cd weather-api
mvn exec:java -Dexec.mainClass="org.ulpgc.dacd.Main"
```

### Ejecutar Event Store Builder

```bash
cd event-store-builder
mvn exec:java -Dexec.mainClass="org.ulpgc.dacd.Main" -Dexec.args="tcp://localhost:61616 event-store-builder Flight,SpaceWeather"
```

> ¡¡Importante!!: El broker ActiveMQ debe estar ejecutándose en `tcp://localhost:61616`

---

## Tecnologías

- Java 21
- Maven (multimódulo)
- ActiveMQ 6.2.4
- SQLite + JDBC
- Gson
- OpenSky Network API
- NOAA Space Weather API

---

## Autores

Adrián Santana Rosales
Nira Armas Maestre