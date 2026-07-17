# Gateway — Medilabo Solutions

Point d’entrée API (Spring Cloud Gateway MVC). Auth HTTP Basic, routage vers les microservices back.

## Prérequis

- Java 21
- Maven 3.9+
- Profil **local** (défaut) : aucune DB externe (H2 fichier)
- Profil **mysql** : MySQL avec base `medilabo_gateway` et user `medilabo` / `medilabo`

## Commandes

```bash
# Lancer (profil local = H2 persistant dans ./data)
mvn spring-boot:run

# Lancer avec MySQL
mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"

# Tests
mvn test

# Package JAR
mvn -DskipTests package
java -jar target/gateway-0.0.1-SNAPSHOT.jar
```

Service disponible sur **http://localhost:8080**.

## Comptes applicatifs (seed au démarrage)

| Identifiant | Mot de passe | Usage |
|-------------|--------------|--------|
| `doctor`    | `password`   | Médecin |
| `service`   | `service`    | Compte technique inter-services |

## Endpoints utiles

| Méthode | Chemin | Description |
|---------|--------|-------------|
| `GET` | `/auth/me` | Vérifie l’auth, renvoie `{ "username": "..." }` |
| `*` | `/patient/**` | Proxy vers `service/patient` (`localhost:8081`) |

## Base de données

- Schéma auto : `src/main/resources/db/schema.sql` (`spring.sql.init`)
- Seed users : `UserDataLoader` (mots de passe BCrypt)
- MySQL (Docker / script) :

```bash
# Depuis la racine du repo
docker compose up -d mysql
# ou
.\scripts\setup-mysql.ps1 -RootPassword "TON_MOT_DE_PASSE_ROOT"
```

## Docker

```bash
docker build -t medilabo-gateway .
docker run --rm -p 8080:8080 medilabo-gateway
```
