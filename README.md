# Yajalones App

Servidor para el sistema de transporte de la empresa "Los Yajalones"

## Tabla de Contenidos

* [Descripción](#descripción)
* [Tecnologías](#tecnologías)
* [Requisitos](#requisitos)
* [Instalación](#instalación)
* [Configuración](#configuración)
* [Estructura de Carpetas](#estructura-de-carpetas)
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
    url: [URL_BD]
    username: [USUARIO_BD]
    password: [CONTRASEÑA_BD]

server:
  port: 8081
```

> **Nota**:
> Ajusta `url`, `username` y `password` según tus credenciales locales.

## Estructura de Carpetas

```
├── src
│   ├── main
│   │   ├── java/com/tuempresa/proyecto
│   │   └── resources
│   │       ├── application.yml        # configuración general
│   │       └── application-dev.yml    # configuración local
│   └── test
├── pom.xml (o build.gradle)
└── README.md
```

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
  "turno": {
    "idTurno": 1
  }
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

| Método | Ruta             | Descripción                     |
| ------ | ---------------- | ------------------------------- |
| GET    | `/paquetes`      | Listar todos los paquetes       |
| GET    | `/paquetes/{id}` | Obtener un paquete por su ID    |
| POST   | `/paquetes`      | Crear un nuevo paquete          |
| PUT    | `/paquetes/{id}` | Actualizar un paquete existente |
| DELETE | `/paquetes/{id}` | Eliminar un paquete (por ID)    |

**Crear Paquete** (`POST /paquetes`)

```json
{
  "remitente": "Empresa A",
  "destinatario": "Cliente B",
  "importe": 250.00,
  "contenido": "Documentos",
  "porCobrar": false,
  "estado": true,
  "viaje": {
    "idViaje": 1
  }
}
```

> Al agregar o eliminar un paquete, el servidor actualiza automáticamente los totales del viaje.

---

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
  "viaje": {
    "idViaje": 1
  }
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
  "unidad": {
    "idUnidad": 1
  }
}
```

#### Validación de Origen y Destino

Solo se permiten estas rutas (insensible a mayúsculas/minúsculas):

* **San Cristobal de las Casas** ↔ **Yajalon**
* **Tuxtla Gutierrez** ↔ **Yajalon**



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
  "unidad": { … },
  "pasajeros": [ … ],
  "paquetes": [ … ],
  "totalViaje": 600.75
}
```

> La respuesta incluye los totales calculados en servidor.

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
        this.tarifaYajalonTuxtla   = tarifaYajalonTuxtla;
    }
}
```

*El importe de cada pasajero se calcula automáticamente según el `TipoPasajero` y la ruta seleccionada.*

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

hermano
