# Frontend — Medilabo Solutions

Interface praticien (React + TypeScript + Vite). Consomme l’API via la **gateway** (`localhost:8080`).

## Prérequis

- Node.js 22+
- Gateway démarrée sur le port **8080** (auth HTTP Basic)

## Commandes

```bash
# Installation des dépendances
npm install

# Mode développement (http://localhost:3000)
npm run dev

# Vérification TypeScript + build production
npm run build

# Prévisualiser le build
npm run preview
```

## Proxy Vite (dev)

Les appels `/auth` et `/patient` sont proxifiés vers `http://localhost:8080`.

## Connexion

| Identifiant | Mot de passe |
|-------------|--------------|
| `doctor`    | `password`   |

Session stockée en `sessionStorage` (credentials HTTP Basic).

## Structure

```
src/
├── components/   # atoms / molecules / organisms (SCSS par composant)
├── pages/
├── routes/
├── session/      # AuthContext + login/logout
└── styles/       # tokens + global
```

## Docker

```bash
docker build -t medilabo-frontend .
docker run --rm -p 80:80 medilabo-frontend
```
