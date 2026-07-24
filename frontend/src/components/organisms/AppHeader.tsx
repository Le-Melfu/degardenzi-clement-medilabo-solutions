import { Link } from 'react-router-dom'
import medilaboLogo from '../../assets/images/medilabo_logo.png'
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
        <Link to="/" className="app-header__brand">
          <img
            className="app-header__logo"
            src={medilaboLogo}
            alt=""
            aria-hidden="true"
          />
          <span className="app-header__wordmark">
            <span className="app-header__name">MédiLabo Solutions</span>
            <span className="app-header__tagline">We care for you</span>
          </span>
        </Link>
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
