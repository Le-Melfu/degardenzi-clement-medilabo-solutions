# Sprint 3 — risk-assessment microservice

Calcul du niveau de risque de diabète. **Aucune base de données** : le service lit `patient` et `notes` en HTTP direct (compte `service` / `service`), sans passer par la gateway.

- Port : `8083`
- Endpoint : `GET /risk/{patId}` (`?mode=strict` pour la recette mot à mot)
- Défaut : mode `clinical` (frontières de mot, négation locale, 2 synonymes Vertiges)
- Sécurité : HTTP Basic (`doctor` / `password`, `service` / `service`)

Le navigateur appelle `GET /risk/{id}` via la gateway (`/risk/**` → 8083).

## Précédence

1. Notes d’abord
2. 0 déclencheur → `None`, **pas d’appel patient**
3. Sinon GET patient, puis seuils âge / genre (du plus grave au plus faible)

## Libellés

`None` · `Borderline` · `In Danger` · `Early onset`
