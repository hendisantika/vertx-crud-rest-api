# Vert.x CRUD REST API

[![Java CI with Maven](https://github.com/hendisantika/vertx-crud-rest-api/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/vertx-crud-rest-api/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange.svg)
[![Vert.x](https://img.shields.io/badge/vert.x-5.2.2-purple.svg)](https://vertx.io)
![Maven](https://img.shields.io/badge/Maven-3.10-blue.svg)

A simple CRUD REST API built with [Eclipse Vert.x 5](https://vertx.io) and Vert.x Web.
HTTP requests are handled by a `Router` and forwarded over the **event bus** to the `crud.service` address.

## Tech Stack

| Component        | Version  |
|------------------|----------|
| Java (JDK)       | 25       |
| Eclipse Vert.x   | 5.2.2    |
| JUnit Jupiter    | 6.1.3    |
| Maven (wrapper)  | 3.10.0   |

## Prerequisites

- JDK 25 (e.g. Temurin: `sdk install java 25-tem`)
- No Maven installation needed: use the bundled Maven wrapper (`./mvnw`)

## API Endpoints

The server listens on port **8080**.

| Method   | Path          | Description        | Event bus message                          |
|----------|---------------|--------------------|--------------------------------------------|
| `GET`    | `/items`      | List all items     | `{}`                                       |
| `POST`   | `/items`      | Create a new item  | request body (JSON)                        |
| `PUT`    | `/items/:id`  | Update an item     | request body + `"id"`                      |
| `DELETE` | `/items/:id`  | Delete an item     | `{"action": "delete", "id": "<id>"}`       |

> **Note:** `MainVerticle` only exposes the HTTP layer. A consumer must be registered on the
> `crud.service` event bus address to handle the messages. Without one, the endpoints respond with `500`.

### Examples

```bash
curl http://localhost:8080/items

curl -X POST http://localhost:8080/items \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop", "price": 1500}'

curl -X PUT http://localhost:8080/items/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop Pro", "price": 2000}'

curl -X DELETE http://localhost:8080/items/1
```

## Building

Run the tests:

```bash
./mvnw clean test
```

Package the application (produces `target/vertx-crud-rest-api-1.0.0-SNAPSHOT-fat.jar`):

```bash
./mvnw clean package
```

## Running

With Maven:

```bash
./mvnw clean compile exec:java
```

Or with the fat jar:

```bash
java -jar target/vertx-crud-rest-api-1.0.0-SNAPSHOT-fat.jar
```

The fat jar uses `io.vertx.launcher.application.VertxApplication` as its main class and
deploys `id.my.hendisantika.vertx_crud_rest_api.MainVerticle`.

## Continuous Integration & Dependency Updates

- **GitHub Actions** ([`.github/workflows/maven.yml`](.github/workflows/maven.yml)) builds and tests the project
  on JDK 25 (Temurin) for every push and pull request to `main`, then submits the dependency graph.
- **Dependabot** ([`.github/dependabot.yml`](.github/dependabot.yml)) checks Maven dependencies and GitHub Actions
  daily at 05:00 (Asia/Jakarta). Related updates are grouped (Vert.x, Netty, JUnit, Maven plugins, Actions).
  Vert.x major upgrades are ignored because they usually need a manual code migration.

## Project Structure

```
.
├── .github/
│   ├── dependabot.yml
│   └── workflows/maven.yml
├── src/
│   ├── main/java/id/my/hendisantika/vertx_crud_rest_api/MainVerticle.java
│   └── test/java/id/my/hendisantika/vertx_crud_rest_api/TestMainVerticle.java
├── mvnw / mvnw.cmd
└── pom.xml
```

## Help

- [Vert.x Documentation](https://vertx.io/docs/)
- [Vert.x 5 Migration Guide](https://vertx.io/docs/guides/vertx-5-migration-guide/)
- [Vert.x Stack Overflow](https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15)
- [Vert.x Discord](https://discord.gg/6ry7aqPWXy)

## Author

**Hendi Santika**

- Email: hendisantika@gmail.com
- Telegram: [@hendisantika34](https://t.me/hendisantika34)
- Link: [s.id/hendisantika](https://s.id/hendisantika)
