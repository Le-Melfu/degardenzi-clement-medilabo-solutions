# Medilabo Solutions

Application de depistage du risque de diabete de type 2 — architecture microservices.

## Structure du projet

```
degardenzi-clement-medilabo-solutions/
├── frontend/                  # Client React (independant, consomme l'API via la gateway)
├── service/
│   ├── patient/               # Sprint 1 — CRUD dossier patient (SQL 3NF / MySQL)
│   ├── gateway/               # Sprint 1 — Spring Cloud Gateway
│   ├── notes/                 # Sprint 2 — notes medecin (MongoDB)
│   └── risk-assessment/       # Sprint 3 — calcul du risque diabete
└── docker-compose.yml
```

## Stack

- **Backend** : Spring Boot 3.4, Java 21
- **Gateway** : Spring Cloud Gateway (routage vers URLs locales)
- **Frontend** : React + Vite (partie client independante, pas un microservice back)
- **Bases de donnees** : MySQL (patient, gateway), MongoDB (notes — sprint 2)

## Demarrage local

→ détail : [`scripts/launch/README.md`](scripts/launch/README.md)

**Prérequis MySQL + MongoDB** puis **ordre** : Patient → Notes → Risk assessment → Gateway → Frontend

### MySQL

```powershell
docker compose up -d mysql
# ou
.\scripts\setup-mysql.ps1 -RootPassword "TON_MOT_DE_PASSE_ROOT"
```

Bases : `medilabo_patient`, `medilabo_gateway` — user DB : `medilabo` / `medilabo`.

### Option rapide (PowerShell)

```powershell
.\scripts\launch\start-all.ps1
```

### Option Cursor / VS Code

**Run and Debug** → `Medilabo Full Backend Restart`, ou un service seul (`Patient — restart`, `Notes — restart`, `Risk assessment — restart`, `Gateway — restart`, `Frontend — restart`).

## Sprints

| Sprint | Livrables |
|--------|-----------|
| 1 | patient (CRUD + seed), gateway, frontend (liste + détail + création / édition) |
| 2 | notes + enrichissement gateway / frontend |
| 3 | risk-assessment + Green Code |

## API risque (sprint 3)

`GET /risk/{patId}` via la gateway (`http://localhost:8080`). HTTP Basic `doctor` / `password`.

Le microservice `risk-assessment` (8083) n’a pas de BDD. Il appelle **directement** notes (toujours) puis patient (seulement s’il y a au moins un déclencheur), avec le compte `service` / `service`.

Niveaux : `None`, `Borderline`, `In Danger`, `Early onset`.

## Green Code

Le client demande une politique de sobriété numérique. Les pistes ci-dessous sont **documentées, pas toutes appliquées** — sauf celles déjà dans le code (pas de BDD risque, court-circuit GET patient si 0 déclencheur).

### Déjà en place sur ce projet

- **Pas de base pour `risk-assessment`** : pas de stockage redondant des dossiers / notes, pas de synchro à maintenir.
- **GET patient conditionnel** : si aucune note ne contient de déclencheur, le calcul s’arrête à `None` sans second appel HTTP.
- **Images Docker JRE** (pas JDK) pour l’exécution : moins de couches et d’outils inutiles en runtime.
- **Pas de lib NLP** (stemmer, WordNet, spaCy) : la détection tient en regex bornées, plus prévisibles et plus légères.

### Pistes d’amélioration (non implémentées)

- **Backend** : mutualiser le chargement des notes (aujourd’hui le front les affiche *et* risk-assessment les relit) via un contrat interne plus fin, ou un cache court TTL si la charge le justifie ; pagination si les historiques grossissent.
- **Bases** : index uniquement sur les colonnes / champs réellement filtrés (`patId` côté notes) ; éviter `show-sql` et les logs verbeux hors debug.
- **Frontend** : ne pas refetch patient+notes+risque en triple si une seule ressource a changé (après ajout de note, seul le risque et la liste des notes bougent — déjà le cas).
- **Infra / Docker** : un `docker-compose` unique avec healthchecks pour ne démarrer la gateway qu’après les backs ; multi-stage builds déjà présents ; limiter CPU/RAM des conteneurs en démo.
- **CI/CD** : cacher le dépôt Maven, ne pas relancer toute la matrice de tests sur un changement front.

Objectif du Green Code ici : moins d’octets déplacés, moins de RAM idle, moins de dépendances pour le même besoin métier.
