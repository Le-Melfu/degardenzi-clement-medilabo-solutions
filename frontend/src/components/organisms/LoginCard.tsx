import { Text } from '../atoms/Text'
import { LoginForm } from '../molecules/LoginForm'
import './LoginCard.scss'

type LoginCardProps = {
  onSubmit: (username: string, password: string) => Promise<void>
}

export function LoginCard({ onSubmit }: LoginCardProps) {
  return (
    <section className="login-card">
      <header className="login-card__header">
        <p className="login-card__brand">Medilabo Solutions</p>
        <h1 className="login-card__title">Connexion</h1>
        <Text tone="muted">
          Accédez à l’espace praticien pour consulter les dossiers patients.
        </Text>
      </header>
      <LoginForm onSubmit={onSubmit} />
    </section>
  )
}
