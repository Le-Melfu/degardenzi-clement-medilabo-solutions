# Patient — Medilabo Solutions

Microservice CRUD dossier patient (SQL 3NF MySQL, Spring Security HTTP Basic).

## Prérequis

- Java 21
- Maven 3.9+
- MySQL : base `medilabo_patient` / user `medilabo`

```powershell
docker compose up -d mysql
# ou
.\scripts\setup-mysql.ps1 -RootPassword "TON_MOT_DE_PASSE_ROOT"
```

## Commandes

```bash
cd service/patient
mvn spring-boot:run

# Tests (H2 in-memory, scope test uniquement)
mvn test
```

Service : **http://localhost:8081**

## Comptes (in-memory, BCrypt)

| Identifiant | Mot de passe |
|-------------|--------------|
| `doctor`    | `password`   |
| `service`   | `service`    |

## Endpoints

| Méthode | Chemin | Description |
|---------|--------|-------------|
| `GET` | `/patient` | Liste des patients |
| `GET` | `/patient/{id}` | Détail |
| `POST` | `/patient` | Création |
| `PUT` | `/patient/{id}` | Mise à jour |

Via gateway : mêmes chemins sur `http://localhost:8080` (auth gateway).

## Champs

| Champ | Obligatoire |
|-------|-------------|
| firstName | oui |
| lastName | oui |
| birthDate | oui |
| gender (`M` / `F`) | oui |
| address | non |
| phone | non |

## Jeu de test (Annexe A)

Au premier démarrage (table vide) : TestNone, TestBorderline, TestInDanger, TestEarlyOnset.

## Docker

```bash
docker build -t medilabo-patient .
docker run --rm -p 8081:8081 medilabo-patient
```
