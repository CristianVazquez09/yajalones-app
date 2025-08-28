# Yajalones App

Servidor para el sistema de transporte de la empresa **Los Yajalones**.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Migraciones (Flyway)](#migraciones-flyway)
- [Estructura de Carpetas](#estructura-de-carpetas)
- [Autenticación e Inicio de Sesión (JWT)](#autenticación-e-inicio-de-sesión-jwt)
- [Caja y Corte de Caja por Turno](#caja-y-corte-de-caja-por-turno)
- [Endpoints Principales](#endpoints-principales)
- [Cálculo de Totales y Comisión](#cálculo-de-totales-y-comisión)
- [Enumeraciones](#enumeraciones)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

---

## Descripción

Backend en **Java 21 + Spring Boot 3** que:

- Expone API REST para CRUD de entidades del dominio.
- Se conecta a **PostgreSQL**.
- Aplica lógica de negocio (pasajeros, paquetería y comisión).
- **Registra movimientos de caja por terminal** (ingresos / pendientes / cobros).
- **Cierra cortes de caja por turno** con saldos y conciliación.
- **Audita** quién crea/modifica mediante Spring Data JPA Auditing.

---

## Tecnologías

- Java 21+
- Spring Boot 3.5.x
- Spring Data JPA (+ Auditing)
- Spring Security 6 (JWT con JJWT)
- PostgreSQL 14+
- Flyway 11.x
- Maven

---

## Requisitos

1. **Java 21** (o superior)
2. **PostgreSQL** (local o remoto)
3. **Maven**
4. Clonar este repositorio

---

## Instalación

```bash
git clone https://github.com/CristianVazquez09/yajalones-app.git
cd yajalones-app
mvn clean install
```

---

## Configuración

Crea `src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/yajalones
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate   # Hibernate solo valida; Flyway maneja el esquema
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8081

jwt:
  secret: ${JWT_SECRET}    # define JWT_SECRET en tu entorno

negocio:
  commission-rate: 0.10    # 10% (configurable por entorno)
```

> **Notas**
> - Con Flyway se recomienda `ddl-auto=validate`. Evita `create-drop` en producción.
> - Gestiona credenciales y `JWT_SECRET` vía variables de entorno o secret manager.

**Variables de entorno (ejemplo):**
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/yajalones
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export JWT_SECRET="clave_secreta_de_32+_bytes"
```

---

## Migraciones (Flyway)

Las migraciones viven en `src/main/resources/db/migration`:

```
V1__init_schema.sql
V2__seed_roles_usuarios.sql
V3__alter_*_lo_que_corresponda.sql
V4__caja_movimientos_y_cortes.sql
V5__auditoria_caja_usuarios.sql
```



## Autenticación e Inicio de Sesión (JWT)

El backend usa **JWT** con filtro propio (JJWT) para poblar el `SecurityContextHolder`. Envía el token en el header:

```
Authorization: Bearer <access_token>
```

### Endpoint de Login

| Método | Ruta             | Descripción                     |
|-------:|------------------|----------------------------------|
|  POST  | `/inicio-sesion` | Autentica y devuelve un **JWT** |

**Credenciales de prueba**
```json
{ "nombreUsuario": "Yajalon", "password": "123" }
{ "nombreUsuario": "Tuxtla", "password": "123" }
```

**Respuesta**
```json
{ "access_token": "<jwt>" }
```

> Spring Security expone el usuario autenticado en `SecurityContextHolder`, lo que permite **auditar** automáticamente `createdBy/lastModifiedBy`.

---

## Caja y Corte de Caja por Turno

**Objetivo:** trazar todo el flujo de dinero por **terminal** y **turno**.

### Conceptos

- **Movimiento de Caja** (`movimiento_caja`)
    - `tipo`: `INGRESO` | `PENDIENTE` | `COBRO_PENDIENTE`
    - `categoria`: `PASAJERO` | `PAQUETE`
    - `terminal`: `TUXTLA` | `YAJALON` 
    - `importe`: `numeric(12,2)`
    - Auditoría: `created_by`, `created_date`, `last_modified_by`, `last_modified_date`

- **Corte de Caja** (`corte_caja`)
    - Estado: `ABIERTO` / `CERRADO`
    - Totales del turno:
        - `ingresos` = Σ `INGRESO` en la terminal/ventana
        - `pendientesCreados` = Σ `PENDIENTE`
        - `pendientesCobrados` = Σ `COBRO_PENDIENTE`
        - `saldoInicialPend` = pendientes previos al turno sin cobrar
        - `saldoFinalPend` = `saldoInicial + creados – cobrados`
    - Auditoría + `abierto_por` / `cerrado_por` (FK a `usuario`).

### Flujo típico

1) **Abrir corte** por terminal/turno
2) Registrar **viaje**, **pasajeros**, **paquetes**:
    - `PAGADO` en **origen** ⇒ `INGRESO` en la terminal de origen
    - `DESTINO` ⇒ `PENDIENTE` en la terminal de destino
    - `SCLC` ⇒ `PENDIENTE` en `SCLC`
3) En la terminal destino, **cobrar pendientes** ⇒ crea `COBRO_PENDIENTE`
4) **Cerrar corte** ⇒ calcula y fija totales

### Endpoints de Caja

| Método | Ruta                          | Descripción                                      |
|-------:|-------------------------------|--------------------------------------------------|
|  POST  | `/caja/abrir/{terminal}`      | Abre (o devuelve) corte **ABIERTO**              |
|   GET  | `/caja/pendientes/{terminal}` | Lista **pendientes** sin cobrar en la terminal   |
|  POST  | `/caja/cobrar/{idPendiente}`  | Cobra un **pendiente** (genera `COBRO_PENDIENTE`) |
|  POST  | `/caja/cerrar/{terminal}`     | Cierra corte: fija totales del turno             |

**Ejemplo práctico (cURL)**

```bash
# 1) Abrir corte en TUXTLA y YAJALON
curl -X POST localhost:8081/caja/abrir/TUXTLA  -H "Authorization: Bearer <jwt>" -H "Content-Type: application/json" -d '{"turnoLabel":"Matutino 28-08-2025"}'
curl -X POST localhost:8081/caja/abrir/YAJALON -H "Authorization: Bearer <jwt>" -H "Content-Type: application/json" -d '{"turnoLabel":"Matutino 28-08-2025"}'

# 2) Registrar viaje, pasajeros y paquetes (según reglas arriba)

# 3) Ver pendientes en YAJALON y cobrar uno (idPendiente=502)
curl localhost:8081/caja/pendientes/YAJALON -H "Authorization: Bearer <jwt>"
curl -X POST localhost:8081/caja/cobrar/502 -H "Authorization: Bearer <jwt>" \
     -H "Content-Type: application/json" -d '{"folioRecibo":"YJ-0002"}'

# 4) Cerrar cortes
curl -X POST localhost:8081/caja/cerrar/TUXTLA  -H "Authorization: Bearer <jwt>" -H "Content-Type: application/json" -d '{"turnoLabel":"Matutino 28-08-2025"}'
curl -X POST localhost:8081/caja/cerrar/YAJALON -H "Authorization: Bearer <jwt>" -H "Content-Type: application/json" -d '{"turnoLabel":"Matutino 28-08-2025"}'
```

---

## Endpoints Principales

### Turnos

| Método | Ruta           | Descripción                   |
|-------:|----------------|-------------------------------|
|   GET  | `/turnos`      | Listar todos los turnos       |
|   GET  | `/turnos/{id}` | Obtener un turno por su ID    |
|  POST  | `/turnos`      | Crear un nuevo turno          |
|   PUT  | `/turnos/{id}` | Actualizar un turno existente |

**Crear Turno**
```json
{ "horario": "08:00:00", "activo": true }
```

### Unidades

| Método | Ruta             | Descripción                     |
|-------:|------------------|---------------------------------|
|   GET  | `/unidades`      | Listar todas las unidades       |
|   GET  | `/unidades/{id}` | Obtener una unidad por su ID    |
|  POST  | `/unidades`      | Crear una nueva unidad          |
|   PUT  | `/unidades/{id}` | Actualizar una unidad existente |

**Crear Unidad**
```json
{ "nombre": "Unidad C", "descripcion": "Ruta de prueba", "activo": true, "turno": { "idTurno": 1 } }
```

### Choferes

| Método | Ruta             | Descripción                    |
|-------:|------------------|--------------------------------|
|   GET  | `/choferes`      | Listar todos los choferes      |
|   GET  | `/choferes/{id}` | Obtener un chofer por su ID    |
|  POST  | `/choferes`      | Crear un nuevo chofer          |
|   PUT  | `/choferes/{id}` | Actualizar un chofer existente |

**Crear Chofer**
```json
{ "nombre": "Juan", "apellido": "Pérez", "telefono": "1234567890", "activo": true, "unidad": { "idUnidad": 1 } }
```

### Descuentos

| Método | Ruta          | Descripción              |
|-------:|---------------|--------------------------|
|  POST  | `/descuentos` | Crear un nuevo descuento |

**Crear Descuento**
```json
{ "concepto": "Promoción Verano", "descripcion": "Descuento del 10%", "importe": 150.0 }
```

### Paquetes

| Método | Ruta                                        | Descripción                                                |
|-------:|---------------------------------------------|------------------------------------------------------------|
|   GET  | `/paquetes`                                 | Listar paquetes                                            |
|   GET  | `/paquetes/{id}`                            | Obtener paquete por ID                                     |
|  POST  | `/paquetes`                                 | Crear paquete (asociado a viaje)                           |
|   PUT  | `/paquetes/{id}`                            | Actualizar paquete                                         |
| DELETE | `/paquetes/{id}`                            | Eliminar paquete                                           |
|  POST  | `/paquetes/pendiente`                       | **Crear paquete pendiente** (sin viaje)                    |
|   GET  | `/paquetes/pendientes`                      | **Listar paquetes pendientes**                             |
|   PUT  | `/paquetes/confirmar/{idPaquete}/{idViaje}` | **Confirmar paquete** al viaje                             |
|   PUT  | `/paquetes/baja/{idPaquete}/{idViaje}`      | **Dar de baja** paquete del viaje                          |

**Crear Paquete**
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

### Pasajeros

| Método | Ruta              | Descripción                      |
|-------:|-------------------|----------------------------------|
|   GET  | `/pasajeros`      | Listar pasajeros                 |
|   GET  | `/pasajeros/{id}` | Obtener pasajero por ID          |
|  POST  | `/pasajeros`      | Crear pasajero                   |
|   PUT  | `/pasajeros/{id}` | Actualizar pasajero              |
| DELETE | `/pasajeros/{id}` | Eliminar pasajero                |

**Crear Pasajero**
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

### Viajes

| Método | Ruta                | Descripción                              |
|-------:|---------------------|------------------------------------------|
|   GET  | `/viajes`           | Listar viajes                            |
|   GET  | `/viajes/{idViaje}` | Obtener viaje por ID                     |
|  POST  | `/viajes`           | Crear viaje (metadatos básicos)          |

**Crear Viaje**
```json
{
  "origen": "Yajalón",
  "destino": "Tuxtla Gutiérrez",
  "fechaSalida": "2025-07-18T07:00:00",
  "unidad": { "idUnidad": 1 }
}
```

**Rutas válidas:** Tuxtla ↔ Yajalón (insensible a may/min).

---

## Cálculo de Totales y Comisión

El servidor recalcula en cada cambio de pasajeros/paquetes:

1. **Pasajeros**
    - Tuxtla → Yajalón:
        - `PAGADO` ⇒ **totalPagadoTuxtla**
        - `DESTINO` ⇒ **totalPagadoYajalon**
        - `SCLC` ⇒ **totalPagadoSclc**
    - Yajalón → Tuxtla:
        - `PAGADO` ⇒ **totalPagadoYajalon**
        - `DESTINO` ⇒ **totalPagadoTuxtla**
        - `SCLC` ⇒ **totalPagadoSclc**
    - `totalPasajeros` = Σ importes de pasajeros

2. **Paquetería**
    - `totalPaqueteria` = Σ importes de paquetes
    - `totalPorCobrar` = Σ importes con `porCobrar=true`
    - **Cobrada** = `totalPaqueteria - totalPorCobrar`

3. **Ingreso bruto**
    - `ingresoBruto` = **pasajeros neto por ruta** + **paquetería cobrada**
      (los montos “a destino” y `SCLC` no suman al bruto hasta cobrarse en su terminal)

4. **Comisión**
    - `comision` = `ingresoBruto * commission-rate` (por defecto **10%**, configurable)

5. **Total del viaje**
    - `totalViaje` = `ingresoBruto - comision`

---

## Enumeraciones

`TipoPasajero`
```java
@Getter
public enum TipoPasajero {
  ADULTO(320.0, 350.0),
  NIÑO(290.0, 290.0),
  INCENT_INAPAM(310.0, 310.0);
  private final double tarifaYajalonSanCristobal;
  private final double tarifaYajalonTuxtla;
}
```

`TipoPago`: `SCLC`, `PAGADO`, `DESTINO`  
`TipoMovimiento`: `INGRESO`, `PENDIENTE`, `COBRO_PENDIENTE`  
`Terminal`: `TUXTLA`, `YAJALON`, `SCLC`

---

## Contribuciones

```bash
git checkout -b feature/nombre-feature
git commit -m "feat: descripción breve"
git push origin feature/nombre-feature
```
Abre un PR explicando tu cambio.

---

## Licencia

MIT License (ver [LICENSE](LICENSE))

---

## Comandos rápidos

```bash
# Migrar BD (Flyway)
./mvnw -Dflyway.url=jdbc:postgresql://localhost:5432/yajalones \
       -Dflyway.user=postgres -Dflyway.password=postgres \
       flyway:migrate

# Ejecutar backend
./mvnw clean spring-boot:run
```
