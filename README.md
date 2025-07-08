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
* [Contribuciones](#contribuciones)
* [Licencia](#licencia)

## Descripción

Este proyecto proporciona un back-end en Java con Spring Boot que:

* Expone API REST para las operaciones CRUD necesarias.
* Se conecta a una base de datos PostgreSQL.
* Proporciona métodos para las operaciones del sistema.

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

**Actualizar Chofer** (`PUT /choferes/{id}`)

```json
{
  "nombre": "Carlos",
  "apellido": "Gómez",
  "telefono": "0987654321",
  "activo": false,
  "unidad": {
    "idUnidad": 2
  }
}
```

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
  "activo": true
}
```

**Actualizar Unidad** (`PUT /unidades/{id}`)

```json
{
  "nombre": "Unidad C Modificada",
  "descripcion": "Descripción actualizada",
  "activo": false
}
```

## Contribuciones

1. Crea una rama con tu feature o fix:

   ```bash
   git checkout -b feature/nombre-feature
   ```
2. Haz tus cambios y commitea siguiendo la convención:

   ```bash
   git commit -m "feat: descripción breve de la funcionalidad"
   ```
3. Push a tu rama:

   ```bash
   git push origin feature/nombre-feature
   ```
4. Abre un Pull Request explicando tu cambio.

## Licencia

Este proyecto está bajo la [MIT License](LICENSE)
