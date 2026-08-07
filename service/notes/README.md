# Sprint 2 — notes microservice (MongoDB)

Microservice Spring Boot pour les notes médecin.

- Port : `8082`
- Base : MongoDB (`medilabo_notes`)
- Compte de service : `medilabo` / `medilabo` (`authSource=medilabo_notes`) — root réservé à l'admin Docker
- Endpoints :
  - `GET /notes/patient/{patId}` — historique des notes
  - `POST /notes` — ajout d'une note (`{ patId, note }`)
- Sécurité : HTTP Basic (`doctor` / `password`, `service` / `service`)

Mongo via Docker :

```powershell
docker compose up -d mongo
```

Connexion shell (compte de service) :

```powershell
docker exec -it medilabo-mongo mongosh medilabo_notes -u medilabo -p medilabo --authenticationDatabase medilabo_notes
```
