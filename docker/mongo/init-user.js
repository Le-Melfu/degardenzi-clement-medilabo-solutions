// Compte de service applicatif (pas root)
// Exécuté par Docker au 1er démarrage Mongo uniquement

db = db.getSiblingDB('medilabo_notes');

db.createUser({
  user: 'medilabo',
  pwd: 'medilabo',
  roles: [
    { role: 'readWrite', db: 'medilabo_notes' }
  ]
});
