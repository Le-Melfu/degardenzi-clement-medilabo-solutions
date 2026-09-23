# Gateway — Medilabo Solutions

Point d’entrée API (Spring Cloud Gateway MVC). Auth HTTP Basic, routage vers les microservices via URLs locales.

## Prérequis

- Java 21
- Maven 3.9+
- Service **patient** démarré sur `http://localhost:8081`
- MySQL : base `medilabo_gateway` / user `medilabo`

## Commandes

```bash
cd service/gateway
mvn spring-boot:run

# Tests (H2 in-memory, scope test uniquement)
mvn test

# Package JAR
mvn -DskipTests package
java -jar target/gateway-0.0.1-SNAPSHOT.jar
```

Service : **http://localhost:8080**

## Routage

| Path gateway | URI | Service |
|--------------|-----|---------|
| `/patient/**` | `http://localhost:8081` | patient |
| `/notes/**` | `http://localhost:8082` | notes (sprint 2) |
| `/risk/**` | `http://localhost:8083` | risk-assessment (sprint 3) |
| `/auth/**` | local | gateway (auth) |

## Comptes applicatifs (seed au démarrage)

| Identifiant | Mot de passe | Usage |
|-------------|--------------|--------|
| `doctor`    | `password`   | Médecin |
| `service`   | `service`    | Compte technique inter-services |

## Endpoints utiles

| Méthode | Chemin | Description |
|---------|--------|-------------|
| `GET` | `/auth/me` | Vérifie l’auth, renvoie `{ "username": "..." }` |
| `*` | `/patient/**` | Proxy vers `patient` (8081) |
| `*` | `/notes/**` | Proxy vers `notes` (8082) |
| `*` | `/risk/**` | Proxy vers `risk-assessment` (8083) |

## Logging des appels

```text
[Gateway] REQUEST  --> patient GET /patient/1 user=doctor
[Gateway] RESPONSE <-- patient GET /patient/1 -> 200 (45 ms) user=doctor
```

Couleurs console (ANSI) :
- **service** : gateway bleu, patient vert, notes jaune, risk rouge
- **statut** : 2xx vert, 4xx jaune, 5xx rouge
- **durée** : vert / jaune (≥300 ms) / rouge (≥1 s)
- **user** : magenta

## Base de données

- Schéma auto : `src/main/resources/db/schema.sql`
- Seed users : `UserDataLoader` (BCrypt)
- MySQL :

```bash
docker compose up -d mysql
```

## Docker

```bash
docker build -t medilabo-gateway .
docker run --rm -p 8080:8080 medilabo-gateway
```
