import { useNavigate } from 'react-router-dom'
import { LoginCard } from '../components/organisms/LoginCard'
import { useAuth } from '../session/AuthContext'
import './LoginPage.scss'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (username: string, password: string) => {
    await login({ username, password })
    navigate('/', { replace: true })
  }

  return (
    <div className="login-page">
      <LoginCard onSubmit={handleSubmit} />
    </div>
  )
}
