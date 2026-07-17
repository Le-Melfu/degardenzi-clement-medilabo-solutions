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
          <h1 className="app-header__title">Espace praticien</h1>
        </div>
        <UserMenu username={username} onLogout={onLogout} />
      </div>
    </header>
  )
}
