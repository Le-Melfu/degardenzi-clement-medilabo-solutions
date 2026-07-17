# Launch scripts — Medilabo

## Ordre de démarrage

1. Patient (8081)
2. Gateway (8080)
3. Frontend (3000)

## Scripts PowerShell (depuis la racine du repo)

```powershell
.\scripts\launch\start-patient.ps1          # H2 local (défaut)
.\scripts\launch\start-gateway.ps1          # H2 local (défaut)
.\scripts\launch\start-frontend.ps1
```

MySQL (après `.\scripts\setup-mysql.ps1 -RootPassword "..."`) :

```powershell
.\scripts\launch\start-patient.ps1 -Profile mysql
.\scripts\launch\start-gateway.ps1 -Profile mysql
```

Ouvrir chaque service dans une **nouvelle fenêtre** :

```powershell
.\scripts\launch\start-all.ps1              # H2
.\scripts\launch\start-all.ps1 -Profile mysql
```

## Cursor / VS Code

Menu **Run and Debug** (Ctrl+Shift+D) :

| Config | Rôle |
|--------|------|
| Patient | Microservice patient |
| Gateway | API gateway |
| Frontend | UI React |
| Medilabo — stack back | Patient + Gateway |
| Medilabo — full | Back + Frontend |

Ou **Terminal → Run Task…** : `frontend:dev`, `patient:run`, etc.

## URLs

| Service | URL |
|---------|-----|
| Gateway | http://localhost:8080 |
| Patient | http://localhost:8081 |
| Frontend | http://localhost:3000 |

Login front : `doctor` / `password`
