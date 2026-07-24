import { Text } from '../atoms/Text'
import { LoginForm } from '../molecules/LoginForm'
import medilaboLogo from '../../assets/images/medilabo_logo.png'
import './LoginCard.scss'

type LoginCardProps = {
  onSubmit: (username: string, password: string) => Promise<void>
}

export function LoginCard({ onSubmit }: LoginCardProps) {
  return (
    <section className="login-card">
      <header className="login-card__header">
        <div className="login-card__brand">
          <img
            className="login-card__logo"
            src={medilaboLogo}
            alt=""
            aria-hidden="true"
          />
          <div className="login-card__wordmark">
            <p className="login-card__name">MédiLabo Solutions</p>
            <p className="login-card__slogan">We care for you</p>
          </div>
        </div>
        <h1 className="login-card__title">Connexion</h1>
        <Text tone="muted">
          Accédez à l’espace praticien pour consulter les dossiers patients.
        </Text>
      </header>
      <LoginForm onSubmit={onSubmit} />
    </section>
  )
}
