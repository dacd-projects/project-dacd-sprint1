# Space Weather & Aviation Analysis (Sprint 1)

## Descripción

El objetivo es analizar el impacto del **clima espacial** en la **aviación comercial**, especialmente en rutas transpolares.


## Objetivo

Capturar datos en tiempo real desde dos fuentes independientes:

*  **Clima espacial (NASA DONKI API)**
*  **Vuelos en tiempo real (OpenSky API)**

Los datos se almacenan de forma incremental en una base de datos SQLite para análisis posteriores.


## Arquitectura

El proyecto sigue una arquitectura **multimódulo**:

```
project-dacd-sprint1
│
├── flight-api
├── weather-api
├── persistence
```

###  Componentes

* **Client** → consumo de API
* **Service** → lógica de negocio
* **Repository (DAO)** → persistencia en SQLite


## Base de Datos

### Tabla: flights

* icao
* callsign
* country
* latitude
* longitude
* altitude
* velocity
* last_update
* captured_at

### Tabla: space_weather

* event_type
* kp_index
* start_time
* end_time
* source
* captured_at


## Ejecución

El sistema se ejecuta automáticamente cada hora mediante:

```
ScheduledExecutorService
```

## Alertas

El sistema genera alertas en consola cuando:

*  Kp ≥ 5 → tormenta geomagnética fuerte

Ejemplo:

```
ALERTA: Tormenta geomagnética Kp=6
```


## Tecnologías

* Java 21
* Maven (multimódulo)
* SQLite
* Jackson (JSON parsing)
* OpenSky API
* NASA DONKI API


## Futuro (Sprint 2)

* Correlación clima espacial vs vuelos
* Mapas de calor
* Visualización avanzada


## Autores

Adrián Santana Rosales – ULPGC
Nira Armas Maestre – ULPGC
Grado en Ciencia e Ingeniería de Datos
