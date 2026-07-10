# Medilabo Solutions

Application de depistage du risque de diabete de type 2 — architecture microservices.

## Structure du projet

```
degardenzi-clement-medilabo-solutions/
├── frontend/                  # Client React (independant, consomme l'API via la gateway)
├── service/
│   ├── patient/               # Sprint 1 — CRUD dossier patient (SQL 3NF)
│   ├── gateway/               # Sprint 1 — Spring Cloud Gateway
│   ├── notes/                 # Sprint 2 — notes medecin (MongoDB)
│   └── risk-assessment/       # Sprint 3 — calcul du risque diabete
└── docker-compose.yml         # A venir
```

## Stack

- **Backend** : Spring Boot 3.4, Java 21
- **Gateway** : Spring Cloud Gateway
- **Frontend** : React + Vite (partie client independante, pas un microservice back)
- **Bases de donnees** : MySQL (patient), MongoDB (notes — sprint 2)

## Demarrage local (base actuelle)

### service/patient (port 8081)

```bash
cd service/patient
mvn spring-boot:run
```

### service/gateway (port 8080)

```bash
cd service/gateway
mvn spring-boot:run
```

### frontend (port 3000)

```bash
cd frontend
npm install
npm run dev
```

## Sprints

| Sprint | Livrables |
|--------|-----------|
| 1 | patient, gateway, frontend (liste + detail patient) |
| 2 | notes + enrichissement gateway / frontend |
| 3 | risk-assessment + Green Code |
