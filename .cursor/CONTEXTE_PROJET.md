# Contexte projet — MédiLabo Solutions (version basique)

> Référence légère pour Cursor. Pour le détail complet (user stories, algo de risque, jeux de test, Green Code…) → `.cursor/CONTEXTE_PROJET_ENRICHI.md`

---

## Objectif

Application de **dépistage du risque de diabète de type 2**. Livraison en **3 sprints** sur un repo GitHub.

## Architecture

| Microservice | Rôle | BDD | Sprint |
|---|---|---|---|
| `service/patient` | CRUD dossier patient | SQL 3NF | 1 |
| `service/gateway` | Point d'entrée, routage | — | 1 → enrichi 2/3 |
| `frontend` | UI (patients, notes, risque) | — | 1 → enrichi 2/3 |
| `service/notes` | Notes médecin par patient | MongoDB | 2 |
| `service/risk-assessment` | Calcul risque diabète | — (appels API) | 3 |

- Stack : **Spring Boot** (back), **Spring Cloud Gateway**, **Docker** (1 Dockerfile/service + 1 `docker-compose.yml` global)
- `frontend` et `service/gateway` : **uniques**, jamais recréés — enrichis à chaque sprint

## Contraintes transverses

1. **3NF** pour la base SQL patient
2. **Spring Security** : HTTP Basic, users in-memory, mots de passe **BCrypt** — pas de sign-up, pas de rôles
3. **Green Code** : recherche + section README + brief oral (sprint 3)
4. **Périmètre strict** : user stories uniquement, rien de non demandé

## Sprint 1 — Patient + Gateway + Front

**User stories** : voir, mettre à jour, ajouter les infos personnelles d'un patient.

**Champs patient** : Prénom*, Nom*, Date naissance*, Genre*, Adresse (opt.), Téléphone (opt.)

**Livrables** :
- `service/patient` : CRUD REST, SQL 3NF, Security, 4 patients test, Dockerfile
- `service/gateway` : routage vers `service/patient`, Dockerfile
- `frontend` : liste patients + détail patient, indépendant du back, Dockerfile

## Sprint 2 — Notes médecin

**User stories** : voir l'historique, ajouter une note.

**Contraintes notes** : texte libre, format conservé (sauts de ligne), pas de limite taille, MongoDB, lien via `patId`.

**Livrables** : `service/notes` + mise à jour `frontend` (notes sur page patient) + `service/gateway`.

## Sprint 3 — Évaluation risque

**User story** : consulter le risque de diabète d'un patient.

**Livrables** :
- `service/risk-assessment` : pas de BDD, appelle `service/patient` + `service/notes`, algo exact → voir fichier enrichi
- Mise à jour `frontend` (affichage risque) + `service/gateway`
- Green Code : README + brief

**Niveaux de risque** : `None`, `Borderline`, `In Danger`, `Early onset` — libellés exacts.

## Règles Cursor essentielles

- Vérifier le **sprint** et le **microservice** avant tout endpoint/entité
- `service/risk-assessment` ne stocke rien — consomme les API de `service/patient` et `service/notes`
- Endpoints sensibles → toujours sécurisés
- Notes → format brut préservé
- Bases simples, pas de sur-ingénierie

## Quand consulter le fichier enrichi

- Algorithme de risque (règles, déclencheurs, seuils)
- Jeux de données test (Annexes A, B, C)
- Points d'attention détaillés par sprint
- Docker Compose, Green Code, consignes complètes
