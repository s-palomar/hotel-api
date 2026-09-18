````markdown
# Hotel API

REST API for hotel, room, client and reservation management, developed with **Java 21 and Spring Boot**.

This project started as a DAW (Web Application Development) exercise and evolved into a complete backend application designed to simulate the main operations of a hotel reservation system.

The main objective is not only to build a functional REST API, but also to apply a clean layered architecture and understand how the different components of a Spring Boot application interact.

---

## Features

The API currently supports:

### Hotels

- Create, retrieve, update and delete hotels.
- Retrieve a hotel by ID.
- Manage hotel information such as name, city and category.

### Rooms

- Manage rooms associated with a hotel.
- Define room type, room number, base price and maximum capacity.
- Prevent duplicate room numbers within the same hotel.
- Update room information.
- Check room availability for a specific period.

### Clients

- Manage clients.
- Search clients by DNI.
- Search clients by surname.
- Use existing client information when processing reservation companions.

### Reservations

- Create reservations for a specific hotel and room type.
- Automatically assign an available room according to the requested room type and number of guests.
- Validate room capacity.
- Check room availability based on reservation dates.
- Calculate the total reservation price according to the number of nights and the room's base price.
- Modify reservations using partial updates.
- Cancel reservations.
- Confirm reservations.
- Perform check-in.
- Perform check-out.
- Automatically finalize reservations according to their departure date and status.

### Payments

- Register payments associated with a reservation.
- Accumulate payments.
- Track payment status:
  - `PENDIENTE`
  - `PARCIAL`
  - `COMPLETADO`
- Require at least 50% of the reservation price to confirm a reservation.
- Prevent payments exceeding the reservation total.
- Require full payment before checkout.

### Companions

- Register reservation companions during check-in.
- Store companion information as a snapshot associated with the reservation.
- Reuse current client information when the companion is an existing client.
- Retrieve companions by ID, DNI, surname or reservation.

### Automatic processes

- Automatically cancel pending reservations after their expiration period.
- Automatically finalize reservations on their departure date.
- Handle confirmed reservations without check-in as no-shows.
- Automatically perform checkout for occupied and fully paid reservations.

---

# Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
H2 Database
```

Additional layers are used to keep the API contract and error handling separate from the persistence model:

```text
hotel-api
│
├── controller
├── service
├── repository
├── model
├── dto
├── exception
├── scheduler
└── config
```

### Controller

Responsible for the HTTP layer:

- Receive requests.
- Read path variables and request bodies.
- Call the appropriate service.
- Return the correct HTTP response.

Controllers do not contain the application's business rules.

### Service

Contains the business logic:

- Reservation rules.
- Availability checks.
- Capacity validation.
- Payment rules.
- Reservation state transitions.
- Check-in/check-out logic.
- Price calculation.

### Repository

Responsible for persistence and database queries.

The project uses Spring Data JPA and custom JPQL queries where the business requirements require specific searches, such as room availability.

### Entity / Model

Represents the application's persistent domain objects.

### DTO

The API uses request and response DTOs to avoid exposing the persistence model directly.

Examples:

- `ReservaRequest`
- `ReservaUpdateRequest`
- `ReservaResponse`
- `CheckinRequest`
- `PagoRequest`
- `ClienteRequest`
- `HabitacionResponse`
- `AcompananteRequest`
- `AcompananteResponse`

### Exception handling

Business errors are represented by domain-specific exceptions and handled centrally through `@ControllerAdvice`.

This keeps error responses consistent across the API.

### Scheduler

Time-dependent processes are separated from the business logic.

The scheduler determines **when** a process is executed, while the service determines **what business rules** are applied.

---

# Domain Model

The main entities are:

```text
Hotel
 └── 1:N ──> Habitacion
                 │
                 └── 1:N ──> Reserva
                               │
                               ├── N:1 ──> Cliente
                               │
                               └── 1:N ──> Acompanante
```

## Hotel

Main information:

- `id`
- `nombre`
- `ciudad`
- `categoria`
- `habitaciones`

## Habitacion

Main information:

- `id`
- `tipoHabitacion`
- `numero`
- `precioBase`
- `maxPax`
- `hotel`

## Cliente

Main information:

- `id`
- `dni`
- `nombre`
- `apellidos`
- `email`
- `telefono`
- `nacionalidad`
- `formaPago`

The client's DNI is intended to uniquely identify a client.

## Reserva

Main information:

- `id`
- `fechaCreacion`
- `fechaEntrada`
- `fechaSalida`
- `estadoReserva`
- `habitacion`
- `cliente`
- `precioTotal`
- `numPax`
- `fechaHoraCheckin`
- `fechaHoraCheckout`
- `acompanantes`
- `importePagado`
- `estadoPago`

## Acompanante

A companion is stored as a reservation-specific snapshot.

This allows the reservation to preserve the companion's information at the time of the stay, even if the client's information changes later.

---

# Reservation States

Reservations use the following states:

```text
PENDIENTE
    │
    ▼
CONFIRMADA
    │
    ▼
OCUPADA
    │
    ▼
FINALIZADA
```

A reservation can also become:

```text
PENDIENTE ──> CANCELADA
```

depending on the business rules.

### PENDIENTE

The reservation has been created but has not yet been confirmed.

Pending reservations have an expiration period.

### CONFIRMADA

The reservation has been confirmed after satisfying the required payment condition.

### OCUPADA

The guest has completed the check-in process.

### FINALIZADA

The stay has ended either through manual checkout or automatic finalization.

### CANCELADA

The reservation has been cancelled and no longer blocks room availability.

---

# Payment States

Payments use:

```text
PENDIENTE
PARCIAL
COMPLETADO
```

A reservation must have at least **50% of its total price paid before it can be confirmed**.

Checkout requires the reservation to be completely paid.

---

# Room Availability

Room availability is calculated using the reservation dates.

Two reservations overlap when:

```text
existingEntry < requestedExit
AND
existingExit > requestedEntry
```

This means that a reservation ending on the same day another reservation begins does not create an overlap.

Cancelled reservations do not block room availability.

When modifying a reservation, the current reservation is excluded from the availability check so that it does not conflict with itself.

---

# Reservation Price

The reservation price is calculated using:

```text
number of nights × room base price
```

For example:

```text
Check-in: 2026-09-10
Check-out: 2026-09-13

3 nights × 170 € = 510 €
```

The price is recalculated when a reservation change affects the room or the stay dates.

---

# Check-in

Check-in is available when:

- The reservation exists.
- The reservation is `CONFIRMADA`.
- The current date is within the allowed check-in period.
- The client DNI matches the reservation client.
- The number of guests is valid.
- The room capacity is sufficient.
- The reservation has not already been checked in.

When check-in succeeds:

```text
CONFIRMADA
     ↓
  OCUPADA
```

The server records the check-in timestamp.

Companions can also be registered during the check-in process.

---

# Checkout

Manual checkout is available for reservations in `OCUPADA` state.

The reservation must be completely paid.

When checkout succeeds:

```text
OCUPADA
   ↓
FINALIZADA
```

The server records `fechaHoraCheckout`.

Endpoint:

```http
PUT /api/reservas/{id}/checkout
```

---

# Automatic Reservation Finalization

The application uses Spring Scheduling for time-dependent reservation processes.

The scheduler executes the reservation finalization process at the configured time each day.

Two situations are handled:

### Confirmed reservation without check-in

A `CONFIRMADA` reservation reaching its departure date is treated as a no-show and becomes:

```text
CONFIRMADA → FINALIZADA
```

No checkout timestamp is recorded because the guest never checked in.

### Occupied reservation

An `OCUPADA` reservation reaching its departure date is automatically checked out when it has been fully paid:

```text
OCUPADA → FINALIZADA
```

In this case, `fechaHoraCheckout` is recorded.

---

# Error Handling

The API uses domain-specific exceptions combined with a global exception handler.

Examples include:

- `HotelYaExisteException`
- `HotelNoEncontradoException`
- `ReservaNoEncontradaException`
- `ReservaCanceladaException`
- `ReservaNoConfirmadaException`
- `ReservaYaOcupadaException`
- `HabitacionNoDisponibleException`
- `CapacidadHabitacionExcedidaException`
- `ClienteNoEncontradoException`
- `ImporteIncorrectoException`
- `PagoPendienteException`
- `ReservaNoOcupadaException`
- `ReservaNoModificableException`

Errors are handled centrally using:

```java
@ControllerAdvice
```

and returned using a common `ErrorResponse` structure.

This allows the API to return consistent HTTP status codes and error messages.

---

# Main Endpoints

## Hotels

```http
GET    /api/hoteles
GET    /api/hoteles/{id}
POST   /api/hoteles
PUT    /api/hoteles/{id}
DELETE /api/hoteles/{id}
```

## Rooms

```http
GET    /api/habitaciones
GET    /api/habitaciones/{id}
POST   /api/habitaciones
PUT    /api/habitaciones/{id}
DELETE /api/habitaciones/{id}
```

## Clients

```http
GET    /api/clientes
GET    /api/clientes/{id}
GET    /api/clientes/dni/{dni}
GET    /api/clientes/apellidos/{apellidos}
POST   /api/clientes
PUT    /api/clientes/{id}
DELETE /api/clientes/{id}
```

## Reservations

```http
GET    /api/reservas
GET    /api/reservas/{id}
POST   /api/reservas
PATCH  /api/reservas/{id}
DELETE /api/reservas/{id}
```

Reservation actions:

```http
PUT    /api/reservas/{id}/confirmar
PATCH  /api/reservas/{id}/checkin
PUT    /api/reservas/{id}/checkout
POST   /api/reservas/{id}/pagos
```

## Companions

```http
GET /api/acompanantes
GET /api/acompanantes/{id}
GET /api/acompanantes/dni/{dni}
GET /api/acompanantes/apellidos/{apellidos}
GET /api/acompanantes/reserva/{reservaId}
```

---

# Testing

The API has been tested through different business scenarios, including both successful and invalid operations.

Examples:

### Reservation

- Create a valid reservation.
- Invalid dates.
- No available room.
- Insufficient room capacity.
- Update only the number of guests.
- Change room type.
- Change dates.
- Reservation cancellation.
- Reservation confirmation.
- Attempting to confirm an invalid reservation state.

### Check-in

- Valid check-in.
- Check-in before the allowed date.
- Check-in on the departure date.
- Incorrect client DNI.
- Incorrect number of guests.
- Insufficient room capacity.
- Repeated check-in.
- Check-in of cancelled reservation.
- Check-in of an unconfirmed reservation.
- Existing and new companions.

### Payments

- Partial payment.
- Complete payment.
- Payment greater than the reservation total.
- Invalid payment amount.
- Payment for a cancelled reservation.

### Checkout

- Successful checkout.
- Checkout with pending payment.
- Checkout of a non-occupied reservation.
- Repeated checkout.
- Automatic checkout of fully paid occupied reservations.
- Automatic finalization of confirmed reservations without check-in.

---

# Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Application framework |
| Spring Web | REST API |
| Spring Data JPA | Persistence layer |
| Hibernate | ORM |
| H2 Database | Development database |
| Maven | Dependency and build management |
| Jakarta Validation | Input validation |
| DBeaver | Database inspection |
| Git / GitHub | Version control |

---

# Running the Project

## Requirements

- Java 21
- Git
- Maven or the included Maven Wrapper

The project can be started using the Maven Wrapper.

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Once started, the API is available at:

```text
http://localhost:8080
```

---

# Database

The project uses **H2** during development.

The database can be inspected with DBeaver using the configured H2 JDBC connection.

The database is intended for development and demonstration purposes in the current version.

---

# Project Structure

```text
src/
└── main/
    └── java/
        └── com/sdover/hotelapi/
            │
            ├── controller/
            ├── service/
            ├── repository/
            ├── model/
            ├── dto/
            ├── exception/
            ├── scheduler/
            └── config/
```

The exact package structure may evolve as the project grows.

---

# Current Status

## Version 1 — Demo Backend

The first functional version of the backend is complete.

Implemented:

- REST API
- Layered architecture
- JPA persistence
- H2 database
- Hotel management
- Room management
- Client management
- Reservation management
- Room availability
- Capacity validation
- Reservation price calculation
- Reservation states
- Payment management
- Check-in
- Check-out
- Companion management
- DTOs
- Jakarta Validation
- Global exception handling
- Automatic reservation expiration
- Automatic reservation finalization
- Business scenario testing

---

# Future Improvements

The project is intentionally designed so that additional functionality can be introduced later.

Possible improvements include:

- Web-based frontend for the API.
- Docker / Docker Compose deployment.
- Public deployment of the demo.
- Authentication and authorization.
- Role-based access control.
- More advanced reservation search.
- Reservation history.
- More detailed room management.
- Additional payment functionality.
- Production database such as PostgreSQL or MySQL.
- Automated integration tests.
- API documentation with OpenAPI / Swagger.
- Improved monetary handling using `BigDecimal`.
- Production-oriented configuration profiles.

---

# Project Goals

This project has two main objectives.

### Technical objective

Build a complete REST backend using modern Java and Spring Boot practices.

### Learning objective

Understand how the different parts of a Spring Boot application work together:

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
JPA / Hibernate
     ↓
Database
     ↓
Response
```

The project therefore focuses not only on making the application work, but also on understanding the responsibilities of each layer and the flow of data through the application.

---

# Author

**Sdover**

DAW — Desarrollo de Aplicaciones Web

Java · Spring Boot · REST APIs · JPA · SQL

---

## Repository

GitHub:

**s-palomar/hotel-api**
````
