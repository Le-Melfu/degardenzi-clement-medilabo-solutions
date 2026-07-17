import { useState, type FormEvent } from 'react'
import { Button } from '../atoms/Button'
import { Text } from '../atoms/Text'
import { FormField } from './FormField'
import './LoginForm.scss'

type LoginFormProps = {
  onSubmit: (username: string, password: string) => Promise<void>
}

export function LoginForm({ onSubmit }: LoginFormProps) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError(null)
    setIsSubmitting(true)

    onSubmit(username, password).then(
      () => setIsSubmitting(false),
      () => {
        setError('Identifiant ou mot de passe incorrect.')
        setIsSubmitting(false)
      }
    )
  }

  return (
    <form className="login-form" onSubmit={handleSubmit} noValidate>
      <FormField
        id="username"
        label="Identifiant"
        name="username"
        autoComplete="username"
        value={username}
        onChange={(event) => setUsername(event.target.value)}
        required
      />
      <FormField
        id="password"
        label="Mot de passe"
        name="password"
        type="password"
        autoComplete="current-password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        required
      />

      {error ? (
        <Text tone="danger" role="alert" className="login-form__error">
          {error}
        </Text>
      ) : null}

      <Button type="submit" disabled={isSubmitting || !username || !password} className="login-form__submit">
        {isSubmitting ? 'Connexion…' : 'Se connecter'}
      </Button>
    </form>
  )
}
