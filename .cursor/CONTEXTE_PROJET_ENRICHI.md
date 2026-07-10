# Contexte projet — MédiLabo Solutions (version enrichie)

> Détail complet du projet : user stories, algo de risque, annexes de test, Green Code, consignes Cursor.
> Pour une vue rapide → `.cursor/CONTEXTE_PROJET.md`

---

## 1. Résumé du projet

**Client** : société travaillant avec des cliniques et cabinets privés sur le dépistage des risques de maladies.
**Objectif** : développer une application permettant d'aider à détecter le **risque de diabète de type 2** chez un patient.
**Commanditaire interne** : Taylor Waters (Responsable Produit).
**Livraison** : un repository GitHub, en **3 sprints**.

### Architecture générale attendue

- Architecture en **microservices**, chaque microservice étant un projet **Spring Boot** indépendant.
- Un microservice **Gateway** développé avec **Spring Cloud Gateway**, point d'entrée unique vers les microservices back.
- Un microservice **Frontend** (interface utilisateur).
- Chaque microservice doit être **dockerisé** (une image Docker par microservice).
- Les microservices back exposent des **API REST**.

### Exigences transverses imposées par le client (à respecter sur TOUT le projet)

1. **Normalisation des bases de données relationnelles en 3NF** (troisième forme normale), exigence liée aux certifications ISO du client → qualité des données.
2. **Sécurisation de l'accès aux données patients** via **Spring Security** (authentification obligatoire sur les endpoints exposant des données sensibles).
3. **Green Code** : le client met en place une politique de sobriété numérique / protection de l'environnement. Les normes ne sont pas encore stabilisées : il faut mener une **recherche documentée** sur le sujet et in fine :
   - briefer Taylor lors de la réunion finale sur le sujet ;
   - documenter dans le `README.md` du projet des **suggestions concrètes d'actions Green Code** applicables à ce projet précis (backend, base de données, frontend, infra/Docker, CI/CD...).

### Suivi de l'usage de l'IA (exigence pédagogique du parcours)

Un "Journal de l'IA" doit être tenu tout au long du projet (ex. via Notion) pour documenter, projet par projet :
- ce qui a été demandé à l'IA,
- ce que l'IA a produit,
- ce qui en a été fait,
- comment cela a été vérifié.

➡️ **Point d'attention pour Cursor** : à chaque génération de code substantielle, garder une trace synthétique (prompt → résultat → vérification) pour alimenter ce journal a posteriori.

### Précisions importantes issues du "Guide d'étapes clés"

- Le projet reprend essentiellement des notions déjà maîtrisées. Les **vraies nouveautés** sont : la base de données **NoSQL (MongoDB)**, **Spring Cloud Gateway**, et **Docker**.
- Le microservice **front est unique et commun aux trois sprints** : il n'est pas recréé à chaque sprint, il **évolue** au fur et à mesure de l'ajout des besoins.
- Le microservice **`service/gateway`** est également unique et commun aux trois sprints : sa configuration est mise à jour à chaque sprint pour ajouter le routage vers les nouveaux microservices back.
- Les bases de données peuvent rester **simples** (pas de sur-ingénierie) tout en répondant au besoin.
- **Authentification simplifiée** : Spring Security doit servir à contrôler les accès, **mais il n'y a pas besoin de mettre en place d'inscription (sign-up) ni de gestion fine des droits/rôles**. Une authentification HTTP Basic avec des utilisateurs **en mémoire** (in-memory) est suffisante. **Ne pas oublier d'encrypter les mots de passe** (ex. `BCryptPasswordEncoder`).
- ⚠️ **Point de vigilance général répété à chaque étape** : ne pas ajouter de fonctionnalités, écrans ou éléments **non demandés** — rester strictement dans le périmètre défini par les user stories et les consignes.

---

## 2. Découpage en microservices (vue cible en fin de projet)

| Microservice                              | Rôle                                                       | Base de données                                                     | Sprint                                         |
| ----------------------------------------- | ---------------------------------------------------------- | ------------------------------------------------------------------- | ---------------------------------------------- |
| `service/patient`                         | Gestion du dossier patient (CRUD)                          | SQL relationnelle (3NF)                                             | Sprint 1                                       |
| `service/gateway`                         | Point d'entrée unique, routage vers les microservices back | Aucune                                                              | Sprint 1 (créé), mis à jour aux sprints 2 et 3 |
| `frontend`                                | Interface utilisateur (pages patient, notes, risque)       | Aucune (consomme les API via `service/gateway`)                     | Sprint 1 (créé), enrichi aux sprints 2 et 3    |
| `service/notes` (nom indicatif)           | Gestion des notes du médecin par patient                   | NoSQL — MongoDB                                                     | Sprint 2                                       |
| `service/risk-assessment` (nom indicatif) | Calcul du niveau de risque de diabète                      | Aucune base dédiée — interroge `service/patient` et `service/notes` | Sprint 3                                       |

**Sécurité** : `service/patient`, `service/notes`, et l'accès via `service/gateway` doivent être protégés par Spring Security (authentification). Le `service/risk-assessment` interrogeant les autres microservices, il doit gérer la propagation de l'authentification / des appels inter-services de manière sécurisée.

---

## 3. Sprint 1 — Microservice patient, Gateway, première interface utilisateur

### Objectifs du sprint
Mettre en place l'infrastructure de base du projet, réutilisée tout au long du développement :
- Architecture microservices avec dockerisation systématique.
- Un microservice back **`service/patient`**, un microservice **`service/gateway`**, un microservice **`frontend`**.

### Origine du besoin (mail de Ramesh Eliot, client — Abernathy Clinic)

> Résumé fidèle du mail transféré par Taylor (pièce jointe "FWD: Exigences du produit") :
- Le client se concentre principalement sur les **soins préventifs**. De nombreux patients ont de mauvais choix nutritionnels (produits bon marché, riches en sucre/calories) qui augmentent leur exposition à certaines maladies, dont le **diabète de type 2**.
- Objectif de l'outil : aider les médecins à **identifier les patients les plus à risque**.
- **Première brique demandée (Sprint 1)** : se concentrer uniquement sur les **données démographiques** (nom, âge, etc.) afin de pouvoir entrer et revoir les informations de base d'un patient. Le risque de diabète augmente avec l'âge si les mauvaises habitudes alimentaires ne sont pas corrigées.
- (Les notes du médecin et le rapport de risque sont demandés dans la suite du mail — voir sprints 2 et 3 ci-dessous.)

### User stories officielles — Sprint 1 (« Résumé des besoins fonctionnels »)

1. **Vue des infos personnelles des patients**
   *En tant qu'organisateur, j'aimerais voir les informations personnelles de mes patients afin de vérifier leur identité lorsqu'ils arrivent en rendez-vous.*
2. **Mise à jour des informations personnelles**
   *En tant qu'organisateur, j'aimerais mettre à jour les informations personnelles de mes patients lorsqu'ils arrivent en rendez-vous pour que nos informations soient toujours à jour.*
3. **Ajouter des informations personnelles des patients**
   *En tant qu'organisateur, j'aimerais ajouter des informations personnelles aux patients pour qu'ils puissent prendre des rendez-vous.*

**Champs du dossier patient (exhaustif — à respecter strictement) :**

| Champ | Obligatoire ? |
|---|---|
| Prénom | Oui |
| Nom | Oui |
| Date de naissance | Oui |
| Genre | Oui |
| Adresse postale | **Optionnel** |
| Numéro de téléphone | **Optionnel** |

➡️ **Point d'attention** : l'adresse postale et le numéro de téléphone sont **optionnels** pour qu'un dossier patient soit considéré comme valide — ne pas les rendre obligatoires (ni en base, ni en validation applicative/front).

### Détail des livrables

1. **Microservice back `service/patient`**
   - Base de données SQL dédiée, **normalisée en 3NF**.
   - Endpoints REST pour la gestion du dossier patient (CRUD : consultation, création, mise à jour) correspondant exactement aux 3 user stories ci-dessus.
   - La structure de la base peut être mise en place **directement via l'ORM** (ex. génération du schéma par Hibernate/JPA en dev).
   - Jeu de données de test : **4 cas de test patients** fournis (voir Annexe A) → à insérer au démarrage pour pouvoir tester les endpoints avec Postman.
   - Sécurisation via **Spring Security** : authentification **HTTP Basic**, utilisateurs **en mémoire**, mots de passe **encryptés** (pas d'inscription, pas de gestion de droits/rôles).
   - Dockerfile pour packager le service.

2. **Microservice `service/gateway`**
   - Développé avec **Spring Cloud Gateway**.
   - Route les requêtes vers `service/patient`.
   - Simplifie la gestion des accès aux microservices (point d'entrée unique).
   - Dockerfile pour packager le service.

3. **Microservice `frontend`**
   - Première version de l'interface utilisateur, **indépendante** (pas incorporée dans le back).
   - Il suffit qu'elle soit **fonctionnelle pour la démo**, en restant **sobre**. Exemple minimal suffisant : une page listant tous les patients, et une page affichant le détail d'un patient.
   - Dockerfile pour packager le service.

### Points d'attention Sprint 1
- Bien respecter les 3 user stories ci-dessus : elles font foi pour le détail des endpoints et champs du dossier patient — **ne rien ajouter de non demandé**.
- Concevoir le schéma SQL du patient en 3NF dès le départ (éviter la redondance de données, dépendances fonctionnelles propres) car il servira de socle aux sprints suivants.
- **Le front ne doit pas être incorporé dans le microservice back : il doit rester un microservice indépendant**, qui sera mis à jour par les sprints suivants.
- Utiliser `start.spring.io` pour générer les projets Spring Boot et bien choisir les dépendances nécessaires.
- Mettre en place le repository GitHub dès le début du projet.
- Penser à l'authentification dès ce sprint (HTTP Basic + utilisateurs en mémoire + mots de passe encryptés) : elle conditionne l'accès aux données sensibles sur tout le reste du projet.

---

## 4. Sprint 2 — Interface pour les notes du médecin (base NoSQL)

### Contexte fonctionnel
Le client (Ramesh) souhaite pouvoir :
- consulter les notes saisies lors des visites précédentes,
- ajouter des notes pour la visite en cours.

Extrait du mail de Ramesh : *permettre aux médecins d'ajouter des notes sous forme de compte rendu à la suite de la visite d'un patient, afin de suivre les comportements potentiellement à risque.*

### User stories officielles — Sprint 2 (« Résumé des besoins fonctionnels »)

1. **Vue historique du patient**
   *En tant que praticien, je veux voir l'historique des informations de mon patient afin d'avoir en tête les problèmes qu'il a eus par le passé.*
2. **Ajouter une note à l'historique du patient**
   *En tant que praticien, je veux pouvoir ajouter une note d'observation à l'historique du patient afin de vérifier que mes conseils sont suivis d'une séance à l'autre.*

### Contraintes spécifiques aux notes
- Chaque prestataire a son propre style de rédaction, mais une **terminologie médicale commune**.
- **Le format d'origine des notes doit être conservé** (retours à la ligne, mise en forme du texte libre).
- **Pas de limite de taille** sur les notes.
- Choix technique confirmé : base **NoSQL de type MongoDB**, adaptée à ce besoin de texte libre non structuré/semi-structuré.

### Détail des livrables

1. **Nouveau microservice back `service/notes`**
   - Base de données **MongoDB**.
   - Endpoints REST pour la gestion des notes (création, consultation par patient, historique des visites).
   - Jeu de données de test : fichier "cas de tests sprint 2" fourni, correspondant aux 4 patients déjà utilisés au sprint 1 → à utiliser pour peupler MongoDB.
   - Sécurisation via Spring Security, cohérente avec le sprint 1.
   - Dockerfile pour packager le service.

2. **Mise à jour du `frontend`**
   - Afficher les notes du médecin **sur la page patient déjà implémentée au sprint 1**.
   - Rester **sobre** dans le design pour cette première version (pas de surcharge visuelle).

3. **Mise à jour de `service/gateway`**
   - Ajouter le routage vers `service/notes`.

### Points d'attention Sprint 2
- Ne pas normaliser/structurer excessivement le contenu des notes : elles doivent rester du texte libre fidèle à la saisie d'origine (attention à l'échappement/à la conservation des sauts de ligne côté API et affichage front).
- **Il faut identifier comment retrouver les patients à partir des notes qui les concernent** — c'est-à-dire prévoir un champ d'identifiant patient (`patId`/`patient`) dans chaque document MongoDB de note, permettant de faire le lien avec `service/patient` (voir jeu de données Annexe B, où chaque note est associée à un `patId` et à un nom de patient).
- Vérifier la cohérence des identifiants patients entre `service/patient` (SQL) et `service/notes` (MongoDB) puisqu'il n'y a pas de clé étrangère native entre les deux bases — la cohérence référentielle doit être gérée applicativement.
- **Ne pas oublier la dépendance Spring Data MongoDB dans le `pom.xml`.**
- L'utilisation de **Spring Data MongoDB** rendra la communication avec la base simple ; comprendre la différence entre base relationnelle et base NoSQL avant de modéliser les documents.
- **Il n'y a pas de contrainte imposée sur l'interface** pour les notes : une possibilité simple et suffisante est d'afficher les notes concernant un patient sur la page de détail du patient développée au sprint 1.
- Penser à ajouter le jeu de données de test des notes (Annexe B) pour pouvoir tester les endpoints avec Postman.
- Garder la même approche de sécurité (Spring Security, HTTP Basic, utilisateurs en mémoire, mots de passe encryptés) que le sprint 1 pour une expérience homogène.
- Le `frontend` doit être **mis à jour**, pas recréé ; `service/gateway` doit être **reconfiguré** pour router aussi vers `service/notes`.

---

## 5. Sprint 3 — Évaluation du risque de diabète + recherche Green Code

### Objectif fonctionnel
Ajouter un microservice capable d'anticiper/calculer le **niveau de risque de diabète de type 2** d'un patient, à afficher sur la page patient existante.

Extrait du mail de Ramesh (la partie qu'il juge **la plus importante**) : générer un rapport indiquant la **probabilité qu'un patient développe un diabète**, sur la base de facteurs fournis par le client.

### User story officielle — Sprint 3 (« Résumé des besoins fonctionnels »)

**Générer un rapport de diabète**
*En tant que praticien, je veux pouvoir consulter le risque de diabète pour un patient afin de le prévenir si sa santé est potentiellement en danger.*

### Algorithme d'évaluation du risque — RÈGLES EXACTES À IMPLÉMENTER

Un patient a l'un des **4 niveaux de risque suivants** (à respecter tel quel, y compris la casse/le libellé, car le format de renvoi de l'endpoint doit correspondre à ce qui est attendu) :

- `None` — aucun risque
- `Borderline` — risque limité
- `In Danger` — danger
- `Early onset` — apparition précoce

**Liste des termes déclencheurs à rechercher dans les notes du patient** (recherche **insensible à la casse** — voir point de vigilance ci-dessous) :

1. Hémoglobine A1C
2. Microalbumine
3. Taille
4. Poids
5. Fumeur / Fumeuse
6. Anormal
7. Cholestérol
8. Vertiges
9. Rechute
10. Réaction
11. Anticorps

Le **nombre d'occurrences distinctes de déclencheurs** trouvées dans l'ensemble des notes d'un patient (toutes visites confondues) détermine le niveau de risque, combiné à l'**âge** et, pour certains cas, au **genre** du patient :

| Niveau | Condition |
|---|---|
| **None** | Aucun déclencheur trouvé dans les notes du patient. |
| **Borderline** | Entre **2 et 5** déclencheurs, **et** le patient a **plus de 30 ans**. |
| **In Danger** | Dépend de l'âge et du genre :<br>• Homme de **moins de 30 ans** → **3** déclencheurs ;<br>• Femme de **moins de 30 ans** → **4** déclencheurs ;<br>• Patient de **plus de 30 ans** (homme ou femme) → **6 ou 7** déclencheurs. |
| **Early onset** | Dépend de l'âge et du genre :<br>• Homme de **moins de 30 ans** → **au moins 5** déclencheurs ;<br>• Femme de **moins de 30 ans** → **au moins 7** déclencheurs ;<br>• Patient de **plus de 30 ans** (homme ou femme) → **au moins 8** déclencheurs. |

➡️ **Points de vigilance critiques (issus du guide d'étapes clés)** :
- **L'algorithme doit incorporer TOUTES les règles fournies** ci-dessus — aucune simplification.
- **Attention à la casse des notes** : la recherche des termes déclencheurs dans le texte libre des notes doit être **insensible à la casse** (ex. « anormal », « Anormal », « ANORMAL » doivent tous être détectés).
- **Le endpoint doit respecter le format de renvoi attendu** (nom du patient et/ou identifiant, niveau de risque au format `None` / `Borderline` / `In Danger` / `Early onset`).
- Le calcul se base sur le **nombre de déclencheurs distincts** trouvés dans les notes (à clarifier/fixer dans l'implémentation : bien s'appuyer sur les cas de test fournis en Annexe B et C pour valider l'interprétation exacte — ex. « Fumeur »/« Fumeuse » comptent pour un seul déclencheur logique de la liste).

### Détail des livrables

1. **Nouveau microservice back `service/risk-assessment`**
   - **Aucune base de données à créer pour ce microservice.**
   - Doit interroger les deux autres microservices back existants afin d'obtenir, pour un patient donné, les données nécessaires au calcul, qu'il traite ensuite avant de renvoyer l'« assessment » (résultat de l'évaluation des risques) :
     - `service/patient` (âge — calculé à partir de la date de naissance — et genre du patient),
     - `service/notes` (contenu des notes du médecin, à analyser pour en extraire les déclencheurs de risque).
   - Logique de calcul du niveau de risque à implémenter selon les règles exactes détaillées ci-dessus, et à **valider avec la liste des réponses attendues pour les 4 cas de test patients** (voir Annexe C) — traiter cela comme une spécification de recette/tests d'intégration.
   - Sécurisation cohérente avec les autres microservices (HTTP Basic, appels inter-services authentifiés).

2. **Mise à jour du `frontend`**
   - Afficher le niveau de risque calculé sur la page patient.

3. **Mise à jour de `service/gateway`**
   - Ajouter le routage vers `service/risk-assessment`.

4. **Recherche et documentation Green Code**
   - Mener une recherche sur les pratiques de Green Code / sobriété numérique applicables à une architecture Spring Boot microservices + frontend + Docker (ex. : optimisation des requêtes, mise en cache, taille des images Docker, choix de formats de données efficaces, réduction des appels réseau inutiles entre microservices, pagination, lazy loading, dimensionnement des ressources conteneurs, etc.).
   - Rédiger dans le **`README.md` du projet** une section dédiée avec des **suggestions concrètes d'actions Green Code** pour ce projet.
   - Préparer un **brief oral synthétique** sur le Green Code pour la réunion finale avec Taylor.

### Points d'attention Sprint 3
- Le `service/risk-assessment` étant sans base de données, veiller à la performance des appels vers les deux autres microservices (éviter les appels redondants, envisager des appels asynchrones/parallèles si pertinent).
- La cohérence des résultats de calcul de risque doit être validée face aux réponses attendues fournies pour les cas de test — traiter cela comme une spécification de recette/tests automatisés (tests d'intégration).
- Le Green Code n'est pas qu'une note de bas de page : c'est un livrable explicite (section README + brief oral) à ne pas oublier.

---

## 6. Contraintes techniques transverses à respecter tout au long du projet

- **Stack** : Spring Boot pour tous les microservices back, Spring Cloud Gateway pour la gateway.
- **Bases de données** :
  - SQL relationnelle en **3NF** pour `service/patient`.
  - **MongoDB** pour `service/notes`.
  - Aucune base pour `service/risk-assessment` et pour `service/gateway`.
- **Sécurité** : Spring Security sur l'ensemble des microservices exposant des données patients, en **HTTP Basic avec utilisateurs en mémoire et mots de passe encryptés** — pas d'inscription ni de gestion de rôles à implémenter.
- **Conteneurisation** : un `Dockerfile` par microservice ; un **unique** `docker-compose.yml` global pour orchestrer l'ensemble du projet (voir étape 5 ci-dessous).
- **Qualité des données** : toute évolution de schéma SQL doit être vérifiée au regard de la 3NF.
- **Green Code** : à garder en tête dès les choix d'architecture (et pas seulement au sprint 3), même si la documentation formelle est un livrable de fin de projet (voir étape 6 ci-dessous).
- **Documentation** : le `README.md` du repository doit à terme contenir au minimum :
  - présentation de l'architecture,
  - instructions de build/lancement (Docker),
  - description des microservices et de leurs endpoints,
  - section dédiée Green Code avec suggestions d'actions.

### Étape 5 (post-sprints) — Dockerisation complète (guide d'étapes clés)

**Prérequis** : les trois sprints doivent être terminés.
**Résultat attendu** :
- un fichier `docker-compose.yml` unique permettant de lancer **tous** les microservices ;
- une application entièrement fonctionnelle une fois démarrée via ce fichier.

**Recommandations :**
- Chaque microservice a son **propre `Dockerfile`**, mais l'ensemble du projet n'a **qu'un seul `docker-compose.yml`**, contenant toutes les informations nécessaires au déploiement complet du projet.
- Utilisation de **Docker Desktop** recommandée.

**Points de vigilance :**
- La configuration peut être complexe : être vigilant sur la **cohérence des noms** donnés aux microservices (noms de services Docker Compose ↔ noms utilisés dans la configuration Spring Cloud Gateway).
- L'énoncé ne demande pas explicitement de conteneuriser les bases de données, **mais ce n'est pas interdit** de le faire (ex. conteneuriser MongoDB et la base SQL peut simplifier la démo).

### Étape 6 (finale) — Recherche sur le Green Code (guide d'étapes clés)

**Prérequis** : tout le code et la conteneurisation doivent être terminés.
**Résultat attendu :**
- avoir identifié les **enjeux** du Green Code ;
- avoir listé des **recommandations d'amélioration « Green »** du projet dans le fichier `README.md` du repository.

**Recommandations :**
- Il **n'est pas demandé d'appliquer** concrètement les principes du Green Code au code du projet, mais bien d'en **comprendre les enjeux** et d'être capable de les expliquer, notamment :
  - Quel est l'objectif du Green Code ?
  - Comment identifier les parties d'un code qui consomment de la mémoire inutilement ?
- Même s'il s'agit de pistes d'amélioration (non appliquées), il faut avoir un **œil critique** sur le projet réalisé.
- S'appuyer sur des ressources externes en plus des lectures fournies (recherche personnelle attendue).

**Ressources suggérées par le guide :**
- Chapitre "Réduisez l'empreinte écologique de votre site web" du cours "Appliquez les principes du Green IT dans votre entreprise".
- Article "How Green Are Java Best Coding Practices?" (Scitepress).
- Article "Green code : écrivez du code vert !" (Institut du numérique responsable).

---

## 7. Livrables finaux attendus

- Un repository GitHub contenant :
  - `service/patient` (+ Dockerfile)
  - `service/gateway` (+ Dockerfile)
  - `frontend` (+ Dockerfile)
  - `service/notes` (+ Dockerfile)
  - `service/risk-assessment` (+ Dockerfile)
  - `README.md` avec section Green Code
- Une soutenance / réunion finale incluant un **brief sur le Green Code**.

---

## 8. Consignes pour Cursor lors de la génération de code

- Toujours vérifier, avant de générer un nouvel endpoint ou une nouvelle entité, à quel sprint et quel microservice il appartient (voir tableau section 2).
- Ne pas mélanger les responsabilités : `service/risk-assessment` ne doit jamais posséder son propre stockage de données patient/notes — il consomme uniquement les API des deux autres services.
- Respecter la 3NF pour toute évolution du schéma SQL de `service/patient` — justifier explicitement toute dénormalisation si elle devait être proposée.
- Ne jamais implémenter un endpoint sensible (données patient, notes) sans le sécuriser via Spring Security.
- Pour les notes médecin, préserver strictement le format brut du texte saisi (pas de troncature, pas de reformattage automatique).
- Garder à l'esprit les principes de Green Code dans les choix d'implémentation (requêtes efficientes, éviter les appels réseau redondants, images Docker légères, etc.), et signaler dans les réponses les choix pertinents à documenter dans le README.
- À chaque étape significative, produire un résumé court (prompt / résultat / vérification) réutilisable pour le "Journal de l'IA".

---

## 9. Récapitulatif du "Guide d'étapes clés" (progression indicative)

| Étape | Contenu | Progression |
|---|---|---|
| 1 | Prise en main du projet + démarrage Sprint 1 (compréhension du besoin, création du projet Spring Boot patient et sa structure en couches) | 5 % |
| 2 | Microservice patient, gateway (Spring Cloud Gateway) et première partie du front — Sprint 1 | 30 % |
| 3 | Interface pour les notes du médecin + base MongoDB — Sprint 2 | 60 % |
| 4 | Rapport d'évaluation du risque de diabète — Sprint 3 | 80 % |
| 5 | Dockerisation de l'ensemble des microservices (docker-compose) | 85 % |
| 6 | Recherche et documentation Green Code (README) | 100 % |

*(Progression donnée à titre indicatif par le guide pédagogique — peut varier d'un apprenant/développeur à l'autre.)*

---

## 10. Annexe A — Jeu de données de test : Patients (Sprint 1)

À utiliser pour peupler la base SQL de `service/patient`.

| Nom | Prénom | Date de naissance | Genre | Adresse | Téléphone |
|---|---|---|---|---|---|
| TestNone | Test | 1966-12-31 | F | 1 Brookside St | 100-222-3333 |
| TestBorderline | Test | 1945-06-24 | M | 2 High St | 200-333-4444 |
| TestInDanger | Test | 2004-06-18 | M | 3 Club Road | 300-444-5555 |
| TestEarlyOnset | Test | 2002-06-28 | F | 4 Valley Dr | 400-555-6666 |

---

## 11. Annexe B — Jeu de données de test : Notes des médecins (Sprint 2)

À utiliser pour peupler la base MongoDB de `service/notes`. Chaque ligne correspond à un document (une note associée à une visite). Le champ `patId` (1 à 4) correspond respectivement aux patients `TestNone`, `TestBorderline`, `TestInDanger`, `TestEarlyOnset` ci-dessus — il doit permettre de retrouver le patient concerné via `service/patient`.

| patId | Patient | Note |
|---|---|---|
| 1 | TestNone | Le patient déclare qu'il « se sent très bien ». Poids égal ou inférieur au poids recommandé. |
| 2 | TestBorderline | Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement. |
| 2 | TestBorderline | Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale. |
| 3 | TestInDanger | Le patient déclare qu'il fume depuis peu. |
| 3 | TestInDanger | Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé. |
| 4 | TestEarlyOnset | Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments. |
| 4 | TestEarlyOnset | Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps. |
| 4 | TestEarlyOnset | Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé. |
| 4 | TestEarlyOnset | Taille, Poids, Cholestérol, Vertige et Réaction. |

➡️ **Utilisation pour Cursor** : ce jeu de données sert de base de recette pour l'algorithme de risque du sprint 3 — les résultats obtenus doivent correspondre exactement à l'Annexe C.

---

## 12. Annexe C — Résultats attendus de l'évaluation du risque (Sprint 3)

| patId | Patient | Risque attendu |
|---|---|---|
| 1 | TestNone | `None` |
| 2 | TestBorderline | `Borderline` |
| 3 | TestInDanger | `In Danger` |
| 4 | TestEarlyOnset | `Early onset` |

➡️ **Ces 4 résultats constituent le test de recette de référence du microservice `service/risk-assessment`** : toute implémentation de l'algorithme doit être validée contre ces 4 cas avant d'être considérée comme terminée. En cas de divergence, revoir en priorité :
- l'extraction correcte de l'âge à partir de la date de naissance et de la date du jour,
- l'insensibilité à la casse de la recherche des termes déclencheurs,
- le comptage des déclencheurs **distincts** (et non des occurrences brutes) sur l'ensemble des notes du patient.
