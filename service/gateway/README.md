# Gateway — Medilabo Solutions

Point d’entrée API (Spring Cloud Gateway MVC). Auth HTTP Basic in-memory (BCrypt), routage vers les microservices.

## Prérequis

- Java 21
- Maven 3.9+
- Services back démarrés (patient, notes, risk) pour un proxy fonctionnel

## Commandes

```bash
cd service/gateway
mvn spring-boot:run

mvn test

mvn -DskipTests package
java -jar target/gateway-0.0.1-SNAPSHOT.jar
```

Service : **http://localhost:8080**

## Routage

| Path gateway | URI | Service |
|--------------|-----|---------|
| `/patient/**` | `http://localhost:8081` | patient |
| `/notes/**` | `http://localhost:8082` | notes |
| `/risk/**` | `http://localhost:8083` | risk-assessment |
| `/auth/**` | local | gateway (auth) |

## Compte applicatif (in-memory)

| Identifiant | Mot de passe | Usage |
|-------------|--------------|--------|
| `doctor`    | `password`   | Médecin (login UI / API) |

Le compte technique `service` / `service` reste sur patient, notes et risk-assessment pour les appels inter-services (risk → patient/notes).

## Endpoints utiles

| Méthode | Chemin | Description |
|---------|--------|-------------|
| `GET` | `/auth/me` | Vérifie l’auth, renvoie `{ "username": "..." }` |
| `*` | `/patient/**` | Proxy vers `patient` (8081) |
| `*` | `/notes/**` | Proxy vers `notes` (8082) |
| `*` | `/risk/**` | Proxy vers `risk-assessment` (8083) |

## Docker

```bash
docker build -t medilabo-gateway .
docker run --rm -p 8080:8080 medilabo-gateway
```
