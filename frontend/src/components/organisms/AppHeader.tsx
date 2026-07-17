import { Link } from 'react-router-dom'
import { UserMenu } from '../molecules/UserMenu'
import './AppHeader.scss'

type AppHeaderProps = {
  username: string
  onLogout: () => void
}

export function AppHeader({ username, onLogout }: AppHeaderProps) {
  return (
    <header className="app-header">
      <div className="app-header__inner">
        <div className="app-header__brand">
          <p className="app-header__eyebrow">Medilabo Solutions</p>
          <h1 className="app-header__title">
            <Link to="/" className="app-header__title-link">
              Espace praticien
            </Link>
          </h1>
        </div>
        <nav className="app-header__nav">
          <Link to="/" className="app-header__nav-link">
            Patients
          </Link>
          <UserMenu username={username} onLogout={onLogout} />
        </nav>
      </div>
    </header>
  )
}
