import { useNavigate } from 'react-router-dom'
import { AppShell } from '../components/organisms/AppShell'
import { HomeDashboard } from '../components/organisms/HomeDashboard'
import { useAuth } from '../session/AuthContext'

export function HomePage() {
  const { session, logout } = useAuth()
  const navigate = useNavigate()

  if (!session) {
    return null
  }

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <AppShell username={session.username} onLogout={handleLogout}>
      <HomeDashboard />
    </AppShell>
  )
}
