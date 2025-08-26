# Yajalones App

Servidor para el sistema de transporte de la empresa "Los Yajalones"

## Tabla de Contenidos

* [Descripción](#descripción)
* [Tecnologías](#tecnologías)
* [Requisitos](#requisitos)
* [Instalación](#instalación)
* [Configuración](#configuración)
* [Estructura de Carpetas](#estructura-de-carpetas)
* [Autenticación e Inicio de Sesión (JWT)](#autenticación-e-inicio-de-sesión-jwt)
* [Endpoints Principales](#endpoints-principales)
* [Enumeraciones](#enumeraciones)
* [Contribuciones](#contribuciones)
* [Licencia](#licencia)

## Descripción

Este proyecto proporciona un back-end en Java con Spring Boot que:

* Expone API REST para las operaciones CRUD necesarias.
* Se conecta a una base de datos PostgreSQL.
* Aplica lógica de negocio para cálculo automático de tarifas, totales y comisiones.
* Incluye en las respuestas la fecha de salida y los totales calculados en servidor.

## Tecnologías

* Java 21+
* Spring Boot 3.5.3
* PostgreSQL 14+
* Maven
* Spring Data JPA

## Requisitos

1. **Java 21** (o superior)
2. **PostgreSQL** corriendo localmente
3. **Maven** instalado
4. Clonar este repositorio

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/CristianVazquez09/yajalones-app.git
   cd yajalones-app
   ```

2. Instala dependencias con Maven:

   ```bash
   mvn clean install
   ```

## Configuración

Antes de ejecutar, crea el archivo `application-dev.yml` para tu configuración local.
Ubicación: `src/main/resources/application-dev.yml`

```yaml
spring:
   datasource:
      url: [DATABASE_URL]
      username: [DATABASE_USERNAME]
      password: [DATABASE_PASSWORD]

server:
   port: 8081

jwt:
   secret: ${SECRET_KEY}
```

> **Notas**
> • Ajusta `url`, `username` y `password` según tus credenciales locales.
> • Define `jwt.secret` con una clave segura (p. ej., 256 bits).

## Estructura de Carpetas

```
├── src
│   ├── main
│   │   ├── java/com/tuempresa/proyecto
│   │   └── resources
│   │       ├── application.yml        # configuración general
│   │       └── application-dev.yml    # configuración local
│   └── test
├── pom.xml 
└── README.md
```

## Autenticación e Inicio de Sesión (JWT)

Este backend usa **JSON Web Tokens (JWT)** para autenticar y autorizar el acceso a endpoints protegidos. El login genera un token con **vigencia de 24 horas**. Debes enviarlo en el header `Authorization` como `Bearer <token>`.

### Endpoint de Login

| Método | Ruta            | Descripción                     |
| ------ | --------------- | ------------------------------- |
| POST   | `/inicioSesion` | Autentica y devuelve un **JWT** |

**Credenciales de prueba**

```json
{
  "nombreUsuario": "Yajalon",
  "password": "123"
}
```

**Respuesta (200 OK)**

```json
{
  "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVFJBQkFKQURPUiIsInN1YiI6IllhamFsb24iLCJpYXQiOjE3NTQ3NzIzOTksImV4cCI6MTc1NDg1ODc5OX0.AE8jP1FdsQrzPfotdpgijepCGCksyGDMxsMG_NblwVs-UGCe0F1MA6oT3Nyp-MESsMW26kRx-TnwuRTwecqGTw"
}
```

> La API **solo devuelve el token** en `access_token`. Expira **24 horas** después de su emisión.
> **Uso**: `Authorization: Bearer <access_token>`

---

## Endpoints Principales

---

### Turnos

| Método | Ruta           | Descripción                   |
| ------ | -------------- | ----------------------------- |
| GET    | `/turnos`      | Listar todos los turnos       |
| GET    | `/turnos/{id}` | Obtener un turno por su ID    |
| POST   | `/turnos`      | Crear un nuevo turno          |
| PUT    | `/turnos/{id}` | Actualizar un turno existente |

**Crear Turno** (`POST /turnos`)

```json
{
  "horario": "08:00:00",
  "activo": true
}
```

---

### Unidades

| Método | Ruta             | Descripción                     |
| ------ | ---------------- | ------------------------------- |
| GET    | `/unidades`      | Listar todas las unidades       |
| GET    | `/unidades/{id}` | Obtener una unidad por su ID    |
| POST   | `/unidades`      | Crear una nueva unidad          |
| PUT    | `/unidades/{id}` | Actualizar una unidad existente |

**Crear Unidad** (`POST /unidades`)

```json
{
  "nombre": "Unidad C",
  "descripcion": "Ruta de prueba",
  "activo": true,
  "turno": { "idTurno": 1 }
}
```

---

### Choferes

| Método | Ruta             | Descripción                    |
| ------ | ---------------- | ------------------------------ |
| GET    | `/choferes`      | Listar todos los choferes      |
| GET    | `/choferes/{id}` | Obtener un chofer por su ID    |
| POST   | `/choferes`      | Crear un nuevo chofer          |
| PUT    | `/choferes/{id}` | Actualizar un chofer existente |

**Crear Chofer** (`POST /choferes`)

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "1234567890",
  "activo": true,
  "unidad": {
      "idUnidad": 1
   }
}
```

> **Nota**: La unidad puede ser *nula* (no asignada).

---

### Descuentos

| Método | Ruta          | Descripción              |
| ------ | ------------- | ------------------------ |
| POST   | `/descuentos` | Crear un nuevo descuento |

**Crear Descuento** (`POST /descuentos`)

```json
{
  "concepto": "Promoción Verano",
  "descripcion": "Descuento del 10% en viajes seleccionados",
  "importe": 150.0
}
```

---

### Paquetes

| Método | Ruta                                        | Descripción                                                |
| ------ |---------------------------------------------|------------------------------------------------------------|
| GET    | `/paquetes`                                 | Listar todos los paquetes                                  |
| GET    | `/paquetes/{id}`                            | Obtener un paquete por su ID                               |
| POST   | `/paquetes`                                 | Crear un nuevo paquete (asociado a un viaje)               |
| PUT    | `/paquetes/{id}`                            | Actualizar un paquete existente                            |
| DELETE | `/paquetes/{id}`                            | Eliminar un paquete (por ID)                               |
| POST   | `/paquetes/pendiente`                       | **Crear paquete pendiente** (sin viaje asignado)           |
| GET    | `/paquetes/pendientes`                      | **Listar paquetes pendientes**                             |
| PUT    | `/paquetes/confirmar/{idPaquete}/{idViaje}` | **Confirmar** (confirma el paquete al viaje asignado)      |
| PUT    | `/paquetes/baja/{idPaquete}/{idViaje}`      | **Dar de Baja** (da de baja al paquete del viaje asignado) |

**Crear Paquete** (`POST /paquetes`)

```json
{
  "remitente": "Empresa A",
  "destinatario": "Cliente B",
  "importe": 250.00,
  "contenido": "Documentos",
  "porCobrar": false,
  "idViaje": 1
}
```

**Crear Paquete Pendiente** (`POST /paquetes/pendiente`)

```json
{
  "remitente": "Empresa A",
  "destinatario": "Cliente B",
  "importe": 250.00,
  "contenido": "Documentos"
}
```

**Confirmar Paquete Pendiente** (`PUT /paquetes/confirmar/{idPaquete}/{idViaje}`)

**Dar de baja Paquete Pendiente** (`PUT /paquetes/baja/{idPaquete}/{idViaje}`)
> Solo se envían los id del paquete y del viaje por confirmar 

**Listar Paquetes Pendientes** (`GET /paquetes/pendientes`) – ejemplo

```json
[
  {
    "idPaquete": 7,
    "remitente": "Empresa A",
    "destinatario": "Cliente B",
    "importe": 250.00,
    "contenido": "Documentos",
    "porCobrar": true,
    "estado": false
  }
]
```

> **Semántica de `estado`**
> `false` → **pendiente** (sin viaje asignado).
> `true`  → **confirmado** (ya no está pendiente; asignado a un viaje).
> Al **agregar, eliminar o confirmar** paquetes, el servidor actualiza automáticamente los totales del viaje.


### Pasajeros

| Método | Ruta              | Descripción                      |
| ------ | ----------------- | -------------------------------- |
| GET    | `/pasajeros`      | Listar todos los pasajeros       |
| GET    | `/pasajeros/{id}` | Obtener un pasajero por su ID    |
| POST   | `/pasajeros`      | Crear un nuevo pasajero          |
| PUT    | `/pasajeros/{id}` | Actualizar un pasajero existente |
| DELETE | `/pasajeros/{id}` | Eliminar un pasajero (por ID)    |

**Crear Pasajero** (`POST /pasajeros`)

```json
{
  "nombre": "Ana",
  "apellido": "López",
  "tipo": "ADULTO",
  "asiento": 5,
  "tipoPago": "DESTINO",
  "idViaje": 1
}
```

> Al agregar o eliminar un pasajero, el servidor actualiza automáticamente los totales del viaje.
> El **importe** se calcula en servidor según el `TipoPasajero` y la ruta seleccionada.

#### Tipos de pago aceptados

* `SCLC`
* `PAGADO`
* `DESTINO`

---

### Viajes

| Método | Ruta                | Descripción                              |
| ------ | ------------------- | ---------------------------------------- |
| GET    | `/viajes`           | Listar todos los viajes                  |
| GET    | `/viajes/{idViaje}` | Obtener un viaje por su ID               |
| POST   | `/viajes`           | Crear un nuevo viaje (metadatos básicos) |

**Crear Viaje** (`POST /viajes`)

```json
{
  "origen": "Yajalón",
  "destino": "Tuxtla Gutiérrez",
  "fechaSalida": "2025-07-18T07:00:00",
  "unidad": { "idUnidad": 1 }
}
```

#### Validación de Origen y Destino

Solo se permiten estas rutas (insensible a mayúsculas/minúsculas):

* **Tuxtla** ↔ **Yajalon**

**Obtener Viaje** (`GET /viajes/{idViaje}`)

```json
{
  "idViaje": 1,
  "origen": "Yajalón",
  "destino": "Tuxtla Gutiérrez",
  "fechaSalida": "2025-07-18T07:00:00",
  "totalPasajeros": 350.00,
  "totalPaqueteria": 250.75,
  "comision": 340.00,
  "totalPorCobrar": 250.75,
  "totalPagadoYajalon": 350.00,
  "totalPagadoSclc": 0.00,
  "descuento": null,
  "unidad": { "idUnidad": 1, "nombre": "U1", "descripcion": "Unidad en buen estado", "activo": true },
  "pasajeros": [ /* ... */ ],
  "paquetes":  [ /* ... */ ],
  "totalViaje": 600.75
}
```

> La respuesta incluye los totales calculados en servidor.

### Cálculo de totales por ruta y tipo de pago

Los **totales del viaje** se calculan automáticamente en el servidor según **la dirección de la ruta** y **el tipo de pago** de cada pasajero, además del estado de cobro de los paquetes.

#### 1) Asignación de importes por **pasajeros**
- **Ruta Tuxtla → Yajalón**
   - `PAGADO` ➜ **totalPagadoTuxtla**
   - `DESTINO` ➜ **totalPagadoYajalon**
   - `SCLC` ➜ **totalPagadoSclc**
- **Ruta Yajalón → Tuxtla**
   - `PAGADO` ➜ **totalPagadoYajalon**
   - `DESTINO` ➜ **totalPagadoTuxtla**
   - `SCLC` ➜ **totalPagadoSclc**

Además:
- **totalPasajeros** = suma de todos los importes de pasajeros (independiente de dónde se sumen por tipo de pago).

#### 2) Asignación de importes por **paquetes**
- **totalPaqueteria** = suma de `importe` de **todos** los paquetes.
- **totalPorCobrar** = suma de `importe` de los paquetes con `porCobrar = true`.
- Los paquetes con `porCobrar = false` se consideran **cobrados** y participan en el cálculo del **totalViaje**.
- Semántica de `estado`:
   - `estado = false` ➜ **pendiente** (sin viaje asignado).
   - `estado = true`  ➜ **confirmado** (ya no pendiente; asignado a un viaje).

#### 3) Cálculo de **totalViaje**
> **totalViaje** = (suma de **paquetes cobrados** `porCobrar = false`)
> + (suma de **pasajeros** con `PAGADO` **en el origen** de la ruta) − **comision**

> Nota: las bolsas por destino (`DESTINO`) y `SCLC` se contabilizan en sus totales correspondientes, pero **no** se suman para `totalViaje` hasta que efectivamente estén cobrados, de acuerdo con la regla anterior.

---


## Enumeraciones

#### TipoPasajero

```java
@Getter
public enum TipoPasajero {
    ADULTO(320.0, 350.0),
    NIÑO(290.0, 290.0),
    INCENT_INAPAM(310.0, 310.0);

    private final double tarifaYajalonSanCristobal;
    private final double tarifaYajalonTuxtla;

    TipoPasajero(double tarifaYajalonSanCristobal, double tarifaYajalonTuxtla) {
        this.tarifaYajalonSanCristobal = tarifaYajalonSanCristobal;
        this.tarifaYajalonTuxtla = tarifaYajalonTuxtla;
    }
}
```

*El importe por pasajero se calcula automáticamente según el `TipoPasajero` y la ruta seleccionada.*

## Contribuciones

1. Crea una rama con tu feature o fix:

   ```bash
   git checkout -b feature/nombre-feature
   ```
2. Haz tus cambios y commitea siguiendo la convención:

   ```bash
   git commit -m "feat: descripción breve de la funcionalidad"
   ```
3. Haz push a tu rama:

   ```bash
   git push origin feature/nombre-feature
   ```
4. Abre un Pull Request explicando tu cambio.

## Licencia

Este proyecto está bajo la [MIT License](LICENSE).
