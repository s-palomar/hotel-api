# 🏨 Hotel API

> 🚧 **Proyecto en desarrollo activo**
>
> Este repositorio documenta paso a paso la construcción de una API REST con Spring Boot. Cada funcionalidad se desarrolla de forma incremental, priorizando el aprendizaje de la arquitectura, las reglas de negocio y las buenas prácticas frente a la velocidad de implementación.

---

## 📖 Descripción

Proyecto desarrollado con **Spring Boot** como parte de mi proceso de aprendizaje de Java Backend y desarrollo de APIs REST.

El objetivo del proyecto es construir una API para la gestión de hoteles siguiendo una arquitectura limpia y buenas prácticas de desarrollo, evolucionando desde un CRUD básico hasta un sistema con reglas de negocio reales.

---

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Jakarta Validation
- Git y GitHub

---

## 📦 Funcionalidades implementadas

### 🏨 Hoteles

- Crear hotel
- Consultar hotel por ID
- Listar hoteles
- Buscar hoteles por ciudad
- Buscar hoteles por categoría
- Buscar hoteles por ciudad y categoría
- Actualizar hotel
- Eliminar hotel

### 🛏 Habitaciones

- Crear habitaciones asociadas a un hotel
- Consultar habitación por ID
- Listar habitaciones de un hotel
- Actualizar habitación
- Eliminar habitación

### ⚠️ Gestión de errores

- Excepciones personalizadas
- Respuestas HTTP adecuadas
- Validación de datos mediante Jakarta Validation
- Manejo global de excepciones con `@ControllerAdvice`

---

## 🏗️ Arquitectura del proyecto

El proyecto sigue una arquitectura por capas:

```text
src
└── main
    └── java
        └── com.sdover.hotelapi
            ├── controller
            ├── dto
            ├── exception
            ├── model
            ├── repository
            └── service
```

---

## 📚 Documentación

La carpeta **docs** contiene documentación técnica sobre el proyecto:

- Arquitectura
- Reglas de negocio
- Backlog técnico
- Roadmap

---

## 🔨 Estado actual del proyecto

### ✅ Completado

- Módulo Hotel
- Módulo Habitación
- Persistencia con H2
- Relaciones JPA
- DTOs
- Validaciones
- Manejo global de excepciones
- Pruebas mediante archivos `.http`

### 🚧 En desarrollo

- Módulo Reserva

Las reservas incorporarán reglas de negocio como:

- comprobación de disponibilidad de habitaciones;
- prevención de reservas solapadas;
- estados de reserva (`PENDIENTE`, `CONFIRMADA` y `CANCELADA`).

---

## 🎯 Objetivos del proyecto

Más que construir un simple CRUD, este proyecto busca aprender y aplicar conceptos habituales en el desarrollo profesional de aplicaciones backend:

- Programación orientada a objetos
- Arquitectura por capas
- Modelado del dominio
- Buenas prácticas con Spring Boot
- Diseño de APIs REST
- Persistencia con JPA/Hibernate
- Evolución incremental del software

---

## 📅 Roadmap

### Versión 1.0

- ✅ Gestión de hoteles
- ✅ Gestión de habitaciones
- 🚧 Gestión de reservas

### Próximas versiones

- Gestión de clientes
- Check-in / Check-out
- Disponibilidad por fechas
- Historial de reservas
- Estadísticas
- Facturación

---

## 👩‍💻 Autor

Proyecto desarrollado como práctica personal con el objetivo de profundizar en el desarrollo Backend con **Java y Spring Boot**, aplicando criterios de diseño y buenas prácticas utilizadas en entornos profesionales.

---

⭐ Proyecto en evolución continua.