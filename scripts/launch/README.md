# Launch scripts — Medilabo

Chemin de développement local (processus sur la machine). Pour l'évaluation, utiliser `docker compose up --build` décrit dans le README à la racine.

Prérequis : bases du compose (`docker compose up -d mysql mongo`). Compte `medilabo` / `medilabo`. Pas de MySQL installé sur Windows.

## Ordre de démarrage

1. Patient (8081)
2. Notes (8082) — **Mongo doit tourner** (`docker compose up -d mongo`)
3. Risk assessment (8083)
4. Gateway (8080)
5. Frontend (3000)

## Scripts PowerShell (depuis la racine du repo)

```powershell
.\scripts\launch\start-infra.ps1
.\scripts\launch\start-patient.ps1
.\scripts\launch\start-notes.ps1
.\scripts\launch\start-risk-assessment.ps1
.\scripts\launch\start-gateway.ps1
.\scripts\launch\start-frontend.ps1

# Ou tout d'un coup :
.\scripts\launch\start-all.ps1
```

## Cursor / VS Code

**Run and Debug** :

| Config | Rôle |
|--------|------|
| `Patient` / `Notes` / `Risk assessment` / `Gateway` / `Frontend` | Premier démarrage |
| `Patient — restart` | Tue le port 8081 puis relance |
| `Notes — restart` | Tue le port 8082 puis relance |
| `Risk assessment — restart` | Tue le port 8083 puis relance |
| `Gateway — restart` | Tue le port 8080 puis relance |
| `Frontend — restart` | Tue le port 3000 puis relance |
| `Medilabo Full Backend Restart` | Tue 8080+8081+8082+8083 puis relance Patient + Notes + Risk + Gateway |
| `Patient — tests` | `mvn test` patient (terminal dédié) |
| `Gateway — tests` | `mvn test` gateway (terminal dédié) |
| `Medilabo — all tests` | `mvn test` à la racine (tous les modules back) |

Les configs `— restart` killent le process qui écoute le port (même hors debugger), puis relancent un nouveau process.

Ou **Terminal → Run Task…** : `patient:test` / `gateway:test` / `medilabo:test`.

## URLs

| Service | URL |
|---------|-----|
| Gateway | http://localhost:8080 |
| Patient | http://localhost:8081 |
| Notes | http://localhost:8082 |
| Risk assessment | http://localhost:8083 |
| Frontend | http://localhost:3000 |

Login front : `doctor` / `password`

## MongoDB (compte de service)

| Rôle | User | Password | authSource |
|------|------|----------|------------|
| Admin Docker | `root` | `root` | `admin` |
| App (`service/notes`) | `medilabo` | `medilabo` | `medilabo_notes` |

```powershell
docker exec -it medilabo-mongo mongosh medilabo_notes -u medilabo -p medilabo --authenticationDatabase medilabo_notes
```

