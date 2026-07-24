# Launch scripts — Medilabo

Prérequis : MySQL démarré (`docker compose up -d mysql` ou `.\scripts\setup-mysql.ps1`).

## Ordre de démarrage

1. Patient (8081)
2. Gateway (8080)
3. Frontend (3000)

## Scripts PowerShell (depuis la racine du repo)

```powershell
.\scripts\launch\start-patient.ps1
.\scripts\launch\start-gateway.ps1
.\scripts\launch\start-frontend.ps1

# Ou tout d'un coup :
.\scripts\launch\start-all.ps1
```

## Cursor / VS Code

**Run and Debug** :

| Config | Rôle |
|--------|------|
| `Patient` / `Gateway` / `Frontend` | Premier démarrage |
| `Patient — restart` | Tue le port 8081 puis relance |
| `Gateway — restart` | Tue le port 8080 puis relance |
| `Frontend — restart` | Tue le port 3000 puis relance |
| `Medilabo Full Backend Restart` | Tue 8080+8081 puis relance Patient + Gateway |
| `Patient — tests` | `mvn test` patient (terminal dédié) |
| `Gateway — tests` | `mvn test` gateway (terminal dédié) |
| `Medilabo — all tests` | `mvn test` à la racine (gateway + patient) |

Les configs `— restart` killent le process qui écoute le port (même hors debugger), puis relancent un nouveau process.

Ou **Terminal → Run Task…** : `patient:test` / `gateway:test` / `medilabo:test`.

## URLs

| Service | URL |
|---------|-----|
| Gateway | http://localhost:8080 |
| Patient | http://localhost:8081 |
| Frontend | http://localhost:3000 |

Login front : `doctor` / `password`
