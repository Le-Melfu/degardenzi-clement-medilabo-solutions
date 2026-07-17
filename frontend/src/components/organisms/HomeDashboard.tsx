import { PlaceholderBlock } from '../molecules/PlaceholderBlock'
import './HomeDashboard.scss'

export function HomeDashboard() {
  return (
    <div className="home-dashboard">
      <header className="home-dashboard__intro">
        <h2 className="home-dashboard__title">Tableau de bord</h2>
        <p className="home-dashboard__subtitle">
          Les modules patients, notes et évaluation du risque seront branchés ici au fil des sprints.
        </p>
      </header>

      <div className="home-dashboard__grid">
        <PlaceholderBlock
          title="Liste des patients"
          description="Consultation et recherche des dossiers patients (Sprint 1)."
        />
        <PlaceholderBlock
          title="Détail patient"
          description="Fiche patient, mise à jour des informations personnelles."
        />
        <PlaceholderBlock
          title="Notes médecin"
          description="Historique des notes et ajout de compte-rendu (Sprint 2)."
        />
        <PlaceholderBlock
          title="Évaluation du risque"
          description="Niveau de risque de diabète de type 2 (Sprint 3)."
        />
      </div>
    </div>
  )
}
