# Medilabo Solutions

Application de dépistage du risque de diabète de type 2. Quatre microservices Spring Boot, une gateway, et un client React indépendant.

## Démarrage pour l'évaluateur

Prérequis : Docker Desktop lancé.

À la racine du dépôt :

```powershell
docker compose up --build
```

Le premier lancement construit les images (plusieurs minutes). Les services attendent que MySQL et MongoDB soient prêts, puis que chaque API réponde, avant d'ouvrir le client.

| Accès | URL |
| --- | --- |
| Interface | http://localhost:3000 |
| API (gateway) | http://localhost:8080 |

Compte de démonstration : `doctor` / `password`.

Parcours utile :

1. Se connecter. La liste contient les 4 patients de test (TestNone, TestBorderline, TestInDanger, TestEarlyOnset).
2. Ouvrir une fiche : informations, notes du médecin, niveau de risque.
3. Risques attendus : TestNone `None`, TestBorderline `Borderline`, TestInDanger `In Danger`, TestEarlyOnset `Early onset`.
4. Ajouter une note sur une fiche, puis recharger : le texte garde ses sauts de ligne, le risque est recalculé.

Exemple d'appel API :

```powershell
curl -u doctor:password http://localhost:8080/patient
```

Arrêt :

```powershell
docker compose down
```

`docker compose down -v` efface aussi les données MySQL et MongoDB. Au prochain démarrage, les jeux de test sont rechargés.

Les scripts dans `scripts/launch/` servent au développement local (processus lancés sur la machine). Ils ne sont pas le chemin d'évaluation.

## Fonctionnement

Le navigateur ne parle qu'à la gateway. Le client React n'est pas sur le réseau des microservices : son conteneur ne fait que servir les fichiers statiques. L'adresse de l'API est `http://localhost:8080`, le port publié de la gateway.

```
Navigateur  →  frontend :3000     (fichiers statiques)
Navigateur  →  gateway  :8080     (seule porte HTTP)
gateway     →  patient, notes, risk-assessment
risk-assessment → patient + notes (appels directs, compte service)
patient     →  MySQL
notes       →  MongoDB
gateway     →  MySQL (comptes de connexion)
```

### frontend

Client React (Vite), servi par nginx sur le port 3000. Liste des patients, création, modification, fiche avec notes et risque. Il envoie l'authentification HTTP Basic vers la gateway. Il ne connaît pas les ports 8081, 8082 et 8083.

### gateway (8080)

Point d'entrée unique.

| Chemin | Service |
| --- | --- |
| `/patient/**` | patient |
| `/notes/**` | notes |
| `/risk/**` | risk-assessment |
| `/auth/me` | identité de l'utilisateur connecté |

Les comptes `doctor` / `password` et `service` / `service` sont créés au démarrage (mots de passe BCrypt). Pas d'inscription.

### patient (8081)

Dossier patient. MySQL, une table, troisième forme normale. Champs : prénom, nom, date de naissance, genre (`M` ou `F`), adresse et téléphone optionnels. Consultation, création, mise à jour. Au premier démarrage, les 4 patients de l'annexe A sont insérés. Sécurité HTTP Basic, utilisateurs en mémoire.

### notes (8082)

Notes du médecin. MongoDB, texte libre, sans limite de taille, liées au patient par `patId`. Consultation de l'historique et ajout. Au premier démarrage, les 9 notes de l'annexe B sont insérées. Même sécurité que patient.

### risk-assessment (8083)

Aucune base. Pour un patient, il lit les notes, cherche les termes déclencheurs (sans tenir compte de la casse, chacun compté une fois), puis lit le patient seulement s'il y a au moins un terme. Il renvoie `None`, `Borderline`, `In Danger` ou `Early onset`. Les appels vers patient et notes utilisent le compte `service` / `service`.

## Green Code

Le client demande une politique de sobriété numérique. Les pistes ci-dessous sont documentées. Elles ne sont pas toutes appliquées.

### Déjà en place

- Pas de base pour `risk-assessment` : pas de copie des dossiers ni des notes.
- Lecture du patient seulement s'il existe au moins un déclencheur. Sinon le résultat est `None`.
- Images d'exécution en JRE, pas en JDK.
- Détection par recherche des 11 termes de la spécification, sans bibliothèque de traitement du langage.
- Un seul `docker-compose.yml`, avec des contrôles de santé : la gateway démarre après les API, le client après la gateway.

### Pistes non implémentées

- Détection clinique, hors spécification : ignorer les négations (« ne fume pas »), ajouter des synonymes, ou distinguer un mode strict et un mode clinique.
- Éviter que l'écran et le calcul de risque relisent les mêmes notes, par un cache court si la charge le justifie. Paginer les historiques s'ils grossissent.
- Indexer seulement `patId` côté notes. Garder les logs SQL coupés hors debug.
- Limiter le CPU et la RAM des conteneurs en démonstration.
- En intégration continue, mettre en cache le dépôt Maven et ne pas relancer tous les tests back pour un changement de client.

Objectif : moins d'octets déplacés, moins de mémoire inutilisée, moins de dépendances pour le même besoin.
