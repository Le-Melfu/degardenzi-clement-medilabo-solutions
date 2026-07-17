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
└── docker-compose.yml
```

## Stack

- **Backend** : Spring Boot 3.4, Java 21
- **Gateway** : Spring Cloud Gateway (routage vers URLs locales)
- **Frontend** : React + Vite (partie client independante, pas un microservice back)
- **Bases de donnees** : MySQL (patient), MongoDB (notes — sprint 2)

## Demarrage local (base actuelle)

→ détail : [`scripts/launch/README.md`](scripts/launch/README.md)

**Ordre** : Patient → Gateway → Frontend

### Option rapide (PowerShell)

```powershell
.\scripts\launch\start-all.ps1
```

Ou service par service :

```powershell
.\scripts\launch\start-patient.ps1
.\scripts\launch\start-gateway.ps1
.\scripts\launch\start-frontend.ps1
```

### Option Cursor / VS Code

**Run and Debug** → `Medilabo — full (H2 + front)` ou configs individuelles (Patient, Gateway, Frontend).

### MySQL (optionnel)

```powershell
.\scripts\setup-mysql.ps1 -RootPassword "TON_MOT_DE_PASSE_ROOT"
.\scripts\launch\start-all.ps1 -Profile mysql
```

Bases : `medilabo_patient`, `medilabo_gateway` — user DB : `medilabo` / `medilabo`.

## Sprints

| Sprint | Livrables |
|--------|-----------|
| 1 | patient (CRUD + seed), gateway, frontend (liste + détail + création / édition) |
| 2 | notes + enrichissement gateway / frontend |
| 3 | risk-assessment + Green Code |
