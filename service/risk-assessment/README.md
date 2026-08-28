# Sprint 3 — risk-assessment microservice

Calcul du niveau de risque de diabète. **Aucune base de données** : notes d’abord, puis patient seulement s’il y a au moins un déclencheur (compte `service` / `service`).

- Port : `8083`
- Endpoint : `GET /risk/{patId}`
- Détection : termes de la spec, insensible à la casse, déclencheurs distincts
- Sécurité : HTTP Basic (`doctor` / `password`, `service` / `service`)

Le navigateur appelle `GET /risk/{id}` via la gateway (`/risk/**` → 8083).

## Libellés

`None` · `Borderline` · `In Danger` · `Early onset`
